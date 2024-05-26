package bps.doohcar.dtos.locais.responses;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ColetaLocaisResponse(

    @JsonProperty("locais") List<Local> locais,
    @JsonProperty("quantidade") long quantidade

) {

    public static ResponseEntity<Object> success(List<Local> locais, long quantidade){

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");

        return new ResponseEntity<Object>(new ColetaLocaisResponse(locais, quantidade), headers, HttpStatus.OK); 

    }

}
