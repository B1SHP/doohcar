package bps.doohcar.controller;

import static bps.doohcar.utlis.ChamadaUtils.chamadaLocation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
import bps.doohcar.dtos.locais.requests.ColetaRestaurantesRequest;
import bps.doohcar.dtos.locais.responses.ColetaLocaisResponse;
import bps.doohcar.dtos.locais.responses.Dia;
import bps.doohcar.dtos.locais.responses.Local;
import bps.doohcar.dtos.locais.responses.Nota;
import bps.doohcar.dtos.tripadvisorapi.DetailDto;
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

    @Autowired
    private LocalRepository localRepository;

    private String locationUrl = "https://api.content.tripadvisor.com/api/v1/location/search?searchQuery=%s&category=%s&radiusUnit=km&language=pt&radius=1000";
    private String detailsUrl = "https://api.content.tripadvisor.com/api/v1/location/%s/details?language=pt&currency=BRL&key=%s";
    private String photosUrl = "https://api.content.tripadvisor.com/api/v1/location/%s/photos?language=pt&limit=5&offset=0&source=Management&key=%s";
 
    @GetMapping("/coleta")
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
    public ResponseEntity<Object> coletaRestaurantes(@RequestBody ColetaRestaurantesRequest request){

        ResponseEntity<Object> validate = request.validate();

        if(validate != null){

            return validate;

        }

        ResponseEntity<DataLocation> data = chamadaLocation(String.format(locationUrl, request.query(), request.tipo()), DataLocation.class, 
            "key", key,
            "latLong", (request.latitude() + "," + request.longitude())
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
                        detail.subRating().comida().value(), 
                        detail.subRating().atendimento().value(), 
                        detail.subRating().preco().value()
                    );

                } else {

                    nota = new Nota(detail.rating(), null, null, null);

                }

                local = new Local(
                    location.locationId(), 
                    detail.name(), 
                    detail.webUrl(), 
                    detail.phone(), 
                    detail.numReviews(),
                    nota, 
                    dias, 
                    fotos
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

        return ColetaLocaisResponse.success(locais);

    }
    
}
