package bps.doohcar.dtos.diskingressos.response;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ColetaEventosResponse(

    List<Evento> patrocinados,
    @JsonProperty("nao_patrocinados") List<Evento> naoPatrocinados

) {

    public static ResponseEntity<Object> answer(List<Evento> patrocinados, List<Evento> naoPatrocinados){

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");


        return new ResponseEntity<Object>(
            new ColetaEventosResponse(patrocinados, naoPatrocinados),
            headers,
            HttpStatus.OK
        );

    }

}
