package bps.doohcar.dtos.propagandas.requests;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import bps.doohcar.dtos.ResponseObject;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "exclui_propaganda_request"
)
public record ExcluiPropagandaRequest(

    @Schema(
        nullable = false
    )
    @JsonProperty("id") Long id

) {

    public ResponseEntity<Object> validate(){

        if(id == null){

            return ResponseObject.error("A variavel 'id' não pode ser null", HttpStatus.BAD_REQUEST);

        }

        return null;

    }

}
