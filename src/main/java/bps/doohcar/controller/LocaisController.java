package bps.doohcar.controller;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bps.doohcar.dtos.ResponseObject;
import bps.doohcar.dtos.locais.requests.ColetaLocaisRequest;
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
    public ResponseEntity<Object> coletaRestaurantes(@RequestBody ColetaLocaisRequest request, @RequestHeader("key") String key){

        if(key == null || !Pattern.matches(this.key, key)){

            return ResponseObject.error("Chave não autorizada ou null", HttpStatus.UNAUTHORIZED);

        }

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

        long quantidade = locaisRepository.contaLocais(request);

        ResponseEntity<Object> response = ColetaLocaisResponse.success(locais, quantidade);

        return response;

    }
    
}
