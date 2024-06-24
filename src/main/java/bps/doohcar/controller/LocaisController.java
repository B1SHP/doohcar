package bps.doohcar.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bps.doohcar.dtos.ResponseObject;
import bps.doohcar.dtos.locais.requests.AlteraPatrocinioRequest;
import bps.doohcar.dtos.locais.requests.ColetaLocaisRequest;
import bps.doohcar.dtos.locais.responses.AlteraPatrocinioResponse;
import bps.doohcar.dtos.locais.responses.ColetaLocaisResponse;
import bps.doohcar.dtos.locais.responses.Local;
import bps.doohcar.repositories.mysql.LocaisRepository;
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

    @Value("${key}")
    private String key;

    @Autowired
    private LocaisRepository locaisRepository;

    @PatchMapping("/altera_patrocinio")
    @Operation(
        summary = "API UTILIZADA PARA A ALTERAÇÃO DOS LOCAIS",
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
                    schema = @Schema(implementation = AlteraPatrocinioResponse.class)
                )
            )
        }
    )
    public ResponseEntity<Object> alteraPatrocinio(@RequestBody AlteraPatrocinioRequest request, @RequestHeader("key") String key){

        if(key == null || !Pattern.matches(this.key, key)){

            return ResponseObject.error("Chave não autorizada ou null", HttpStatus.UNAUTHORIZED);

        }

        ResponseEntity<Object> validate = request.validate();

        if(validate != null){

            return validate;

        }

        List<Local> locaisAtualizados = new ArrayList<>();

        for(Long id : request.ids()){

            locaisRepository.mudaPatrocinio(id, request.aprovacao());

            locaisAtualizados.add(
                locaisRepository.coleta(id) 
            );

        }

        return AlteraPatrocinioResponse.answer(locaisAtualizados);

    }

    @PostMapping("/coleta_nao_patrocinado")
    @Operation(
        summary = "API UTILIZADA PARA A COLETA DOS LOCAIS",
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
                responseCode = "204",
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
    public ResponseEntity<Object> coletaRestaurantesNaoPatrocinados(@RequestBody ColetaLocaisRequest request, @RequestHeader("key") String key){

        if(key == null || !Pattern.matches(this.key, key)){

            return ResponseObject.error("Chave não autorizada ou null", HttpStatus.UNAUTHORIZED);

        }

        ResponseEntity<Object> validate = request.validate();

        if(validate != null){

            return validate;

        }

        List<Local> locaisNaoPatrocinados = locaisRepository.coletaLocais(request, 0);

        if(locaisNaoPatrocinados == null || locaisNaoPatrocinados.size() < 1){

            return ResponseObject.error(
                "Nenhum local foi encontrado", 
                HttpStatus.NO_CONTENT
            );

        }

        long quantidadeNaoPatrocinados = locaisRepository.contaLocais(request, 0);

        return ColetaLocaisResponse.success(
            null, 
            locaisNaoPatrocinados, 
            null, 
            quantidadeNaoPatrocinados
        );

    }

    @PostMapping("/coleta_patrocinado")
    @Operation(
        summary = "API UTILIZADA PARA A COLETA DOS LOCAIS",
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
                responseCode = "204",
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
    public ResponseEntity<Object> coletaRestaurantesPatrocinados(@RequestBody ColetaLocaisRequest request, @RequestHeader("key") String key){

        if(key == null || !Pattern.matches(this.key, key)){

            return ResponseObject.error("Chave não autorizada ou null", HttpStatus.UNAUTHORIZED);

        }

        ResponseEntity<Object> validate= request.validate();

        if(validate != null){

            return validate;

        }

        List<Local> locaisPatrocinados = locaisRepository.coletaLocais(request, 1);

        if(locaisPatrocinados == null || locaisPatrocinados.size() < 1){

            return ResponseObject.error(
                "Nenhum local foi encontrado", 
                HttpStatus.NO_CONTENT
            );

        }

        long quantidadePatrocinados = locaisRepository.contaLocais(request, 1);

        return ColetaLocaisResponse.success(
            locaisPatrocinados, 
            null, 
            quantidadePatrocinados, 
            null
        );

    }
    
}
