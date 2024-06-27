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
        description = "Imagem codificada em base 64",
        nullable = true
    )
    @JsonProperty("imagem") String imagem,

    @Schema(
        nullable = false
    )
    @JsonProperty("url_redirecionamento") String urlRedirecionamento,

    @Schema(
        nullable = false,
        description = "1 -> sobre ; 2 -> tempo ; 3 -> espelho ; 4 -> inicial ; 5 -> restaurantes ; 6 -> disk"
    )
    @JsonProperty("tela_de_display") Integer telaDeDisplay

) {

    public ResponseEntity<Object> validate(){

        String message = null;

        if(titulo == null || titulo.length() < 1){

            message = "A variavel 'titulo' não pode ser null";

        } else if((urlVideo == null || urlVideo.length() < 1 ) && (imagem == null || imagem.length() < 1)){

            message = "Pelo menos uma das urls precisa ser utilizada, 'url_video' ou 'url_imagem'";

        } else if(urlRedirecionamento == null || urlRedirecionamento.length() < 1){

            message = "A variavel 'url_redirecionamento' não pode ser null";

        } else if(telaDeDisplay == null && imagem != null){

            message = "A variavel 'tela_de_display' não foi adicionada";

        } 

        if(telaDeDisplay != null){

            if(telaDeDisplay < 1 || telaDeDisplay > 6){

                message = "A variavel 'tela_de_display' não pode ser menor do que 1 ou maior do que 6";

            }

        }

        return message == null ? null : ResponseObject.error(message, HttpStatus.BAD_REQUEST);

    }

}
