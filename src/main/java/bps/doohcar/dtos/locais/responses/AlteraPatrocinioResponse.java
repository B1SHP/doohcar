package bps.doohcar.dtos.locais.responses;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "altera_patrocinio_response"
)
public record AlteraPatrocinioResponse(

    List<Local> locais

) {

    public static ResponseEntity<Object> answer(List<Local> locais){

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");

        return new ResponseEntity<Object>(
            new AlteraPatrocinioResponse(locais),
            headers,
            HttpStatus.OK
        );

    }

}
