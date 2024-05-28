package bps.doohcar.dtos.propagandas.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "cria_propaganda_response"
)
public record CriaPropagandaResponse(

    @JsonProperty("id") long id

) {

    public static ResponseEntity<Object> answer(long id){

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new CriaPropagandaResponse(id))
        ;

    }

}
