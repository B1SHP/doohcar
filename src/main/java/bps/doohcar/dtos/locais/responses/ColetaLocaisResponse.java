package bps.doohcar.dtos.locais.responses;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ColetaLocaisResponse(

    @JsonProperty("locais") List<Local> locais

) {

    public static ResponseEntity<Object> success(List<Local> locais){

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ColetaLocaisResponse(locais))
        ;

    }

}
