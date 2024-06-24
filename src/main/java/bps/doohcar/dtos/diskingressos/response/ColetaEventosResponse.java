package bps.doohcar.dtos.diskingressos.response;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ColetaEventosResponse(

    List<Evento> eventos 

) {

    public static ResponseEntity<Object> answer(List<Evento> eventos){

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");


        return new ResponseEntity<Object>(
            new ColetaEventosResponse(eventos),
            headers,
            HttpStatus.OK
        );

    }

}
