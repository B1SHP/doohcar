package bps.doohcar.dtos.locais.requests;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

import bps.doohcar.dtos.ResponseObject;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "coleta_restaurantes_request"
)
public record ColetaRestaurantesRequest(

    @Schema(
        nullable = false
    )
    @JsonProperty("latitude") Double latitude,

    @Schema(
        nullable = false
    )
    @JsonProperty("longitude") Double longitude,
    
    @Schema(
        nullable = false,
        description = "1: Restaurantes, 2: Atrações"
    )
    @JsonProperty("tipo_lugar") Integer tipoLugar,

    @Schema(
        nullable = false,
        example = "Festas"
    )
    @JsonProperty("query") String query

) {

    public String tipo(){

        return switch(tipoLugar){
            case 1 -> "restaurants";
            case 2 -> "attractions";
            default -> null;
        };

    }

    public ResponseEntity<Object> validate(){

        String message = null;

        if(latitude == null){

            message = "A variavel 'latitude' não pode ser null";

        }

        if(longitude == null){

            message = "A variavel 'longitude' não pode ser null";

        }

        if(tipoLugar == null){

            message = "A variavel 'tipo_lugar' não pode ser null";

        }

        if(query == null){

            message = "A variavel 'query' não pode ser null";

        }

        return message == null ? null : ResponseObject.error(message, HttpStatus.BAD_REQUEST);

    }

}
