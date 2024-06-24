package bps.doohcar.dtos.propagandas.requests;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

import bps.doohcar.dtos.ResponseObject;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "altera_propaganda_request"
)
public record AlteraPropagandaRequest(

    @Schema(
        nullable = false
    )
    @JsonProperty("id") Long id,

    @Schema(
        nullable = true
    )
    @JsonProperty("titulo") String titulo,

    @Schema(
        nullable = true
    )
    @JsonProperty("url_video") String urlVideo,

    @Schema(
        description = "Imagem codificada em base 64",
        nullable = true
    )
    @JsonProperty("imagem") String imagem,

    @Schema(
        nullable = true
    )
    @JsonProperty("url_redirecionamento") String urlRedirecionamento

) {

    public ResponseEntity<Object> validate(){

        String message = null;

        if(id == null){

            message = "A variavel 'id' não pode ser null";

        } 

        if(titulo == null && urlVideo == null && imagem == null & urlRedirecionamento == null){

            message = "Nenhum item foi adicionado ao corpo da request";

        }

        return ResponseObject.error(message, HttpStatus.BAD_REQUEST);

    }

}
