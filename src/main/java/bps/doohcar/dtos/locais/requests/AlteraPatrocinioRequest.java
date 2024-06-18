package bps.doohcar.dtos.locais.requests;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import bps.doohcar.dtos.ResponseObject;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "altera_patrocinio_request"
)
public record AlteraPatrocinioRequest(

    List<Long> ids,

    @Schema(
        description = "1 -> Aprovado, 2 -> Não Aprovado"
    )
    Integer aprovacao

) {

    public ResponseEntity<Object> validate(){

        String message = null;

        if(ids == null || ids.size() < 1){

            message = "A variavel 'ids' não pode ser null ou vazia";

        } else if(aprovacao == null || aprovacao < 0 || aprovacao > 1){

            message = "A variavel 'aprovacao' não pode ser null, menor que 0 ou maior que 1";

        }

        return message == null ? null : ResponseObject.error(message, HttpStatus.BAD_REQUEST);

    }

}
