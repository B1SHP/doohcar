//package bps.doohcar.dtos.propagandas.requests;
//
//import org.springframework.http.ResponseEntity;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//
//@Schema(
//    name = "altera_propaganda_request"
//)
//public record AlteraPropagandaRequest(
//
//    @Schema(
//        nullable = false
//    )
//    @JsonProperty("id") Long id,
//
//    @Schema(
//        nullable = true
//    )
//    @JsonProperty("titulo") String titulo,
//
//    @Schema(
//        nullable = true
//    )
//    @JsonProperty("url_video") String urlVideo,
//
//    @Schema(
//        nullable = true
//    )
//    @JsonProperty("url_imagem") String urlImagem,
//
//    @Schema(
//        nullable = true
//    )
//    @JsonProperty("url_redirecionamento") String urlRedirecionamento
//
//) {
//
//    public ResponseEntity<Object> validate(){
//
//        String message = null;
//
//        if(id == null){
//
//
//
//        }
//
//    }
//
//}
