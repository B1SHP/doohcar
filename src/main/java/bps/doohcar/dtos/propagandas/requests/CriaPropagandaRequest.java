package bps.doohcar.dtos.propagandas.requests;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import bps.doohcar.dtos.ResponseObject;

@Schema(
    name = "cria_propaganda_request"
)
public record CriaPropagandaRequest(

    @Schema(
        nullable = false
    )
    @JsonProperty("titulo") String titulo,

    @Schema(
        nullable = true
    )
    @JsonProperty("url_video") String urlVideo,

    @Schema(
        nullable = true
    )
    @JsonProperty("url_imagem") String urlImagem,

    @Schema(
        nullable = false
    )
    @JsonProperty("url_redirecionamento") String urlRedirecionamento

) {

    public ResponseEntity<Object> validate(){

        String message = null;

        if(titulo == null || titulo.length() < 1){

            message = "A variavel 'titulo' não pode ser null";

        } else if((urlVideo == null || urlVideo.length() < 1 ) && (urlImagem == null || urlImagem.length() < 1)){

            message = "Pelo menos uma das urls precisa ser utilizada, 'url_video' ou 'url_imagem'";

        } else if(urlRedirecionamento == null || urlRedirecionamento.length() < 1){

            message = "A variavel 'url_redirecionamento' não pode ser null";

        }

        return message == null ? null : ResponseObject.error(message, HttpStatus.BAD_REQUEST);

    }

}
