package bps.doohcar.dtos.locais.responses;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record ColetaLocaisResponse(

    @JsonProperty("locais_patrocinados") List<Local> locaisPatrocinados,
    @JsonProperty("locais_nao_patrocinados") List<Local> locaisNaoPatrocinados,
    @JsonProperty("quantidade_patrocinados") Long quantidadePatrocinados,
    @JsonProperty("quantidade_nao_patrocinados") Long quantidadeNaoPatrocinados

) {

    public static ResponseEntity<Object> success(List<Local> locaisPatrocinados, List<Local> locaisNaoPatrocinados, Long quantidadePatrocinados, Long quantidadeNaoPatrocinados){

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");

        return new ResponseEntity<Object>(
            new ColetaLocaisResponse(
                locaisPatrocinados, 
                locaisNaoPatrocinados, 
                quantidadePatrocinados, 
                quantidadeNaoPatrocinados
            ), 
            headers, 
            HttpStatus.OK
        ); 

    }

}
