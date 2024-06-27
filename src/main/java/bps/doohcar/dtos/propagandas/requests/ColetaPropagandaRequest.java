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
    @JsonProperty("offset") Long offset,

    @Schema(
        nullable = true,
        description = "1 -> sobre ; 2 -> tempo ; 3 -> espelho ; 4 -> inicial ; 5 -> restaurantes ; 6 -> disk"
    )
    @JsonProperty("tela") Integer tela

) {

    public ResponseEntity<Object> validate(){

        String message = null;

        if(id == null && limit == null && offset == null && tela == null){

            message = "Nenhuma variavel foi adicionada ao corpo da request";

        } else if(limit == null && offset != null){

            message = "A variavel 'offset' só pode ser utilizada junto com a variavel 'limit'";

        } else if(limit != null && offset == null){

            message = "A variavel 'limit' só pode ser utilizada junto com a variavel 'offset'";

        }

        return message == null ? null : ResponseObject.error(message, HttpStatus.BAD_REQUEST);

    }

}
