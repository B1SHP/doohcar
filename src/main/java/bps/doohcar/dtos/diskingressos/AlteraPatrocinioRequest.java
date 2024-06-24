package bps.doohcar.dtos.diskingressos;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

import bps.doohcar.dtos.ResponseObject;
import io.swagger.v3.oas.annotations.media.Schema;

public record AlteraPatrocinioRequest(

    @Schema(
        nullable = false,
        description = "0 -> Não patrocinado, 1 -> Patrocinado"
    )
    @JsonProperty("patrocinado") Integer patrocinado,

    @Schema(
        nullable = false,
        description = "Ids que seram modificados"
    )
    @JsonProperty("ids") List<String> ids

) {

    public ResponseEntity<Object> validate(){

        String message = null;

        if(patrocinado == null){

            message = "A variavel 'patrocinado' não pode ser null";

        } else if(ids == null){

            message = "A variavel 'ids' não pode ser null";

        } else if(ids.size() < 1){

            message = "A variavel 'ids' não pode estar vazia";

        }

        return message == null ? null : ResponseObject.error(message, HttpStatus.BAD_REQUEST); 

    }

}
