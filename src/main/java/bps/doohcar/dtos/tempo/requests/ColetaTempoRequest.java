package bps.doohcar.dtos.tempo.requests;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

import bps.doohcar.dtos.ResponseObject;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "coleta_tempo_request"
)
public record ColetaTempoRequest(

    @Schema(
        nullable = false
    )
    @JsonProperty("latitude") Double latitude,

    @Schema(
        nullable = false
    )
    @JsonProperty("longitude") Double longitude

) {

    public ResponseEntity<ResponseObject> validate(){

        if(latitude == null){

            return ResponseObject.error(
                "A variavel 'latitude' não pode ser null", 
                HttpStatus.BAD_REQUEST
            );

        }

        if(longitude == null){

            return ResponseObject.error(
                "A variavel 'longitude' não pode ser null", 
                HttpStatus.BAD_REQUEST
            );

        }

        return null;

    }

}
