package bps.doohcar.dtos.propagandas.geral;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "propaganda"
)
public record PropagandaDto(

    @JsonProperty("id") long id,
    @JsonProperty("titulo") String titulo,
    @JsonProperty("url_video") String urlVideo,
    @JsonProperty("url_imagem") String urlImagem,
    @JsonProperty("url_redirecionamento") String urlRedirecionamento

) {

}
