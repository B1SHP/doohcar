package bps.doohcar.controller;

import static bps.doohcar.utlis.ChamadaUtils.chamadaLocation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import bps.doohcar.dtos.ResponseObject;
import bps.doohcar.dtos.locais.DataLocation;
import bps.doohcar.dtos.locais.DataPhotos;
import bps.doohcar.dtos.locais.Image;
import bps.doohcar.dtos.locais.Location;
import bps.doohcar.dtos.locais.requests.ColetaLocaisRequest;
import bps.doohcar.dtos.locais.responses.ColetaLocaisResponse;
import bps.doohcar.dtos.locais.responses.Dia;
import bps.doohcar.dtos.locais.responses.Local;
import bps.doohcar.dtos.locais.responses.Nota;
import bps.doohcar.dtos.tripadvisorapi.Cuisine;
import bps.doohcar.dtos.tripadvisorapi.DetailDto;
import bps.doohcar.repositories.mysql.LocaisRepository;
import bps.doohcar.repositories.redis.LocalRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/dooh-car/local")
@Tag(
    name = "APIs DOOH-CAR: LOCAIS",
    description = "CONTÉM TODOS OS ENDPOINTS RELACIONADOS AOS LOCAIS"
)
public class LocaisController {

    @Value("${trip-advisor.api.key}")
    private String key;

    @Value("${inicio.latitude}")
    private double inicioLatitude;

    @Value("${inicio.longitude}")
    private double inicioLongitude;

    @Value("${fim.latitude}")
    private double fimLatitude;

    @Value("${fim.longitude}")
    private double fimLongitude;

    @Autowired
    private LocaisRepository locaisRepository;

    @Autowired
    private LocalRepository localRepository;

    private String locationUrl = "https://api.content.tripadvisor.com/api/v1/location/nearby_search?category=%s&radiusUnit=km&language=pt&radius=1000";
    private String detailsUrl = "https://api.content.tripadvisor.com/api/v1/location/%s/details?language=pt&currency=BRL&key=%s";
    private String photosUrl = "https://api.content.tripadvisor.com/api/v1/location/%s/photos?language=pt&limit=5&offset=0&source=Management&key=%s"; 

    @GetMapping("/popula")
    public ResponseEntity<Object> popula() throws JsonProcessingException{

        double inicioLatitude2 = inicioLatitude;
        double inicioLongitude2 = inicioLongitude;

        List<String> chamadas = new ArrayList<>();

        boolean precisaContinuar = false;
        long exceptions = 0;
        long adicionados = 0;

        //japonesa, mexicana, churrasco, japones, batata, hamburger, italizana, pizza, macarrão

        do {

            try {

                if(precisaContinuar){
                    System.out.println("asdasd");

                    Thread.sleep(30000);
                
                }

                while(inicioLongitude2 <= fimLongitude){

                    while(inicioLatitude2 <= fimLatitude){

                        System.out.println("Exceptions: " + exceptions + ", Adicionados: " + adicionados);

                        chamadas.add(inicioLatitude2 + ", " + inicioLongitude2);

                        inicioLatitude2 += 0.005f;

                        List<Local> locais = pegaLocais(inicioLatitude2, inicioLongitude2);

                        for(Local local : locais){

                            Long id = locaisRepository.coleta(local.id());

                            if(id == null){

                                locaisRepository.adicionaLocais(local, 1);
                                adicionados++;

                            } else {System.out.println("Ja ta la");}

                        }

                    }

                    inicioLongitude2 += 0.005f;
                    inicioLatitude2 = inicioLatitude;

                }

                precisaContinuar = false;

			} catch (Exception e) {

                e.printStackTrace();
                precisaContinuar = true;
                exceptions++;

			}

        } while(precisaContinuar);

        return ResponseEntity.ok(chamadas);

    }

    public List<Local> pegaLocais(double latitude, double longitude){

        ResponseEntity<DataLocation> data = chamadaLocation(
            String.format(
                locationUrl, 
                "restaurants"
            ),
            DataLocation.class, 
            "key", key,
            "latLong", (latitude + "," + longitude)
        );

        List<Local> locais = new ArrayList<>();

        for(Location location : data.getBody().locations()){

            Local local = null;

            try {

				local = localRepository.collectCache(location.locationId());

			} catch (JsonProcessingException e) {

                System.out.println("Erro ao coletar a cache");

			}

            if(local == null){

                ResponseEntity<DetailDto> responseEntityDetail = new RestTemplate().getForEntity(
                    String.format(detailsUrl, location.locationId(), key), 
                    DetailDto.class
                );

                System.out.println("Details: " + String.format(detailsUrl, location.locationId(), key));

                ResponseEntity<DataPhotos> responseEntityDataPhotos = new RestTemplate().getForEntity(
                    String.format(photosUrl, location.locationId(), key), 
                    DataPhotos.class
                );

                DetailDto detail = responseEntityDetail.getBody();
                DataPhotos dataPhotos = responseEntityDataPhotos.getBody();

                ArrayList<Dia> dias = new ArrayList<>();

                if(detail.hours() != null){

                    detail.hours().periods().forEach(
                        period -> {
                            dias.add(
                                new Dia(period.open().day(), period.open().time(), period.close().time())
                            );
                        }
                    );    

                }

                ArrayList<Image> fotos = new ArrayList<>();

                dataPhotos.imageObject().forEach(
                    image -> {
                        fotos.add(image.image());
                    }
                );

                Nota nota = null;

                if(detail.subRating() != null){

                    nota = new Nota(
                        detail.rating(), 
                        detail.subRating().comida() == null ? null : detail.subRating().comida().value(), 
                        detail.subRating().atendimento() == null ? null : detail.subRating().atendimento().value(), 
                        detail.subRating().preco() == null ? null : detail.subRating().preco().value()

                    );

                } else {

                    nota = new Nota(detail.rating(), null, null, null);

                }

                ArrayList<String> cozinha = new ArrayList<>();

                if(detail.cuisines() != null && detail.cuisines().size() > 0){

                    for(Cuisine cuisine : detail.cuisines()){

                        cozinha.add(cuisine.localizedName());

                    }

                }

                local = new Local(
                    location.locationId(), 
                    detail.name(), 
                    detail.webUrl(), 
                    detail.phone(), 
                    detail.numReviews(),
                    nota, 
                    dias, 
                    fotos,
                    cozinha,
                    1
                );

                try {

					localRepository.createCache(location.locationId(), local);

				} catch (JsonProcessingException e) {

                    System.out.println("Erro ao criar cacher");

				}

                System.out.println("No cache which is sad asf");

            } else {System.out.println("Cache yo");}

            locais.add(local);

        }

        return locais;

    } 
 
    @PostMapping("/coleta")
    @Operation(
        summary = "API UTILIZADA PARA A COLETA DO TEMPO",
        responses = {
            @ApiResponse(
                responseCode = "400",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "500",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = ColetaLocaisResponse.class)
                )
            )
        }
    )
    public ResponseEntity<Object> coletaRestaurantes(@RequestBody ColetaLocaisRequest request){

        ResponseEntity<Object> validate= request.validate();

        if(validate != null){

            return validate;

        }

        List<Local> locais = locaisRepository.coletaLocais(request);

        if(locais == null || locais.size() < 1){

            return ResponseObject.error(
                "Nenhum local foi encontrado", 
                HttpStatus.NOT_FOUND
            );

        }

        ResponseEntity<Object> response = ColetaLocaisResponse.success(locais);

        return response;

    }
    
}
