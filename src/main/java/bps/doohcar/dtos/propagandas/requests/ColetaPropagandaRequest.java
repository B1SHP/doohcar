package bps.doohcar.dtos.propagandas.requests;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import bps.doohcar.dtos.ResponseObject;

@Schema(
    name = "coleta_propaganda_request"
)
public record ColetaPropagandaRequest(

    @Schema(
        nullable = true
    )
    @JsonProperty("id") Long id,

    @Schema(
        nullable = true
    )
    @JsonProperty("limit") Long limit,

    @Schema(
        nullable = true
    )
    @JsonProperty("offset") Long offset

) {

    public ResponseEntity<Object> validate(){

        String message = null;

        if(id == null && limit == null && offset == null){

            message = "Nenhuma variavel foi adicionada ao corpo da request";

        } else if(limit == null && offset != null){

            message = "A variavel 'offset' só pode ser utilizada junto com a variavel 'limit'";

        } else if(limit != null && offset == null){

            message = "A variavel 'limit' só pode ser utilizada junto com a variavel 'offset'";

        }

        return message == null ? null : ResponseObject.error(message, HttpStatus.BAD_REQUEST);

    }

}
