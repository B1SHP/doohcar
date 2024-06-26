package bps.doohcar.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import bps.doohcar.dtos.ResponseObject;
import bps.doohcar.dtos.diskingressos.AlteraPatrocinioRequest;
import bps.doohcar.dtos.diskingressos.DiskIngressoResponse;
import bps.doohcar.dtos.diskingressos.Hit;
import bps.doohcar.dtos.diskingressos.Source;
import bps.doohcar.dtos.diskingressos.response.ColetaEventosResponse;
import bps.doohcar.dtos.diskingressos.response.Evento;
import bps.doohcar.repositories.mysql.EventosRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/dooh-car/eventos")
@Tag(
    name = "APIs DOOH-CAR: EVENTOS",
    description = "CONTÉM TODOS OS ENDPOINTS RELACIONADOS AOS INGRESSOS"
)
public class DiskIngressosController {

    @Value("${key}")
    private String key;

    @Autowired 
    private EventosRepository eventosRepository;

    private static final RestTemplate restTemplate = new RestTemplate();

    @PutMapping("/altera")
    @Operation(
        summary = "API UTILIZADA PARA A ALTERAÇÃO DOS PATROCINIOS",
        responses = {
            @ApiResponse(
                responseCode = "400",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            )
        }
    )
    public ResponseEntity<Object> alteraPatrocinio(AlteraPatrocinioRequest request, @RequestHeader("key") String key){

        if(key == null || !Pattern.matches(this.key, key)){

            return ResponseObject.error("Chave não autorizada ou null", HttpStatus.UNAUTHORIZED);

        }

        ResponseEntity<Object> validate = request.validate();

        if(validate != null){

            return validate;

        }

        for(String id : request.ids()){

            eventosRepository.alteraPatrocinio(id, request.patrocinado());

        }

        return ResponseObject.success("Eventos alterados com sucesso", HttpStatus.OK);

    }

    @GetMapping("/coleta")
    @Operation(
        summary = "API UTILIZADA PARA A COLETA DOS EVENTOS",
        responses = {
            @ApiResponse(
                responseCode = "400",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse( responseCode = "401",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = ColetaEventosResponse.class)
                )
            )
        }
    )
    public ResponseEntity<Object> coletaEventos(@RequestHeader("key") String key){

        if(key == null || !Pattern.matches(this.key, key)){

            return ResponseObject.error("Chave não autorizada ou null", HttpStatus.UNAUTHORIZED);

        }

        ResponseEntity<DiskIngressoResponse> diskIngressoResponseEntity = restTemplate.postForEntity(
            "https://www.diskingressos.com.br/home/_search?size=1000&from=0", 
            "{ \"aggs\": { \"filtered_state\": { \"filter\": { \"terms\": { \"city\": [ \"Curitiba\" ] } }, \"aggs\": { \"state\": { \"terms\": { \"field\": \"state\" } } } }, \"city\": { \"terms\": { \"field\": \"city\", \"size\": 1000 } }, \"filtered_local\": { \"filter\": { \"terms\": { \"city\": [ \"Curitiba\" ] } }, \"aggs\": { \"local\": { \"terms\": { \"field\": \"local\", \"size\": 1000 } } } }, \"filtered_classification\": { \"filter\": { \"terms\": { \"city\": [ \"Curitiba\" ] } }, \"aggs\": { \"classification\": { \"terms\": { \"field\": \"classification\", \"size\": 1000 } } } } }, \"filter\": { \"terms\": { \"city\": [ \"Curitiba\" ] } }, \"query\": { \"filtered\": { \"query\": { \"match_all\": {} }, \"filter\": { \"bool\": { \"must\": [ { \"range\": { \"finalsale\": { \"gte\": \"now\" } } } ] } } } }, \"sort\": [ { \"data\": { \"order\": \"asc\" } } ] }",
            DiskIngressoResponse.class
        );

        if(!diskIngressoResponseEntity.hasBody()){

            return ResponseObject.error("Nenhum evento foi coletado", HttpStatus.INTERNAL_SERVER_ERROR);

        } 

        List<Evento> patrocinados = new ArrayList<>();
        List<Evento> naoPatrocinados = new ArrayList<>();

        for(Hit hit : diskIngressoResponseEntity.getBody().hits().hits()){

            Source source = hit.source();

            String url = null;

            if(source.groupId() == 0){

                url = "https://www.diskingressos.com.br/event/" + source.id();

            } else {

                url = new StringBuilder("https://www.diskingressos.com.br/grupo/")
                    .append(source.groupId()).append("/")
                    .append(source.date()).append("/")
                    .append(source.state()).append("/")
                    .append(source.city()).append("/")
                    .append(source.eventName())
                .toString();

            }

            boolean patrocinado = false;

            if(!eventosRepository.verificaSeOEventoJaExiste(hit.id())){

                eventosRepository.cria(
                    hit.id(),
                    source.eventName() 
                );  

            } else {

                patrocinado = eventosRepository.verificaSeEPatrocinado(hit.id());

            }

            String urlDisk = "http://localhost:7000/api/v1/dooh-car/redirect/redireciona?tipo=3&id=" + hit.id() + "&key=" + key + "&url=" + url; 

            if(patrocinado){

                patrocinados.add(
                    new Evento(
                        hit.id(), 
                        source.date(), 
                        source.eventName(), 
                        "https://api.diskingressos.com.br" + source.image(), 
                        urlDisk,
                        source.state(), 
                        source.city(),
                        source.local()
                    )
                );

            } else {

                naoPatrocinados.add(
                    new Evento(
                        hit.id(), 
                        source.date(), 
                        source.eventName(), 
                        "https://api.diskingressos.com.br" + source.image(), 
                        urlDisk,
                        source.state(), 
                        source.city(),
                        source.local()
                    )
                );

            }

        }

        return ColetaEventosResponse.answer(patrocinados, naoPatrocinados);

    }

    
}
