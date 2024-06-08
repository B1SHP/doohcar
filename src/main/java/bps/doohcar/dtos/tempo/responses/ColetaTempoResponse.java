package bps.doohcar.dtos.tempo.responses;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import bps.doohcar.dtos.ResponseObject;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "coleta_tempo_response"
)
@JsonInclude(Include.NON_NULL)
public class ColetaTempoResponse extends ResponseObject {

    @JsonProperty("cidade") 
    private String cidade;

    @JsonProperty("temperatura") 
    private List<TemperaturaDto> temperaturaDto;

    @JsonProperty("nascer_do_sol")
    private String nascerDoSol;

    @JsonProperty("por_do_sol")
    private String porDoSol;

    private ColetaTempoResponse(String cidade, List<TemperaturaDto> temperaturaDto, String nascerDoSol, String porDoSol){

        this.cidade = cidade;
        this.temperaturaDto = temperaturaDto;
        this.nascerDoSol = nascerDoSol;
        this.porDoSol = porDoSol;
        
        super.setSuccess("Tempo encontrado com sucesso");

    }

    public static ResponseEntity<Object> success(String cidade, List<TemperaturaDto> temperaturaDto, String nascerDoSol, String porDoSol){

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");

        return new ResponseEntity<Object>(
            new ColetaTempoResponse(
                cidade, 
                temperaturaDto, 
                nascerDoSol, 
                porDoSol
            ), 
            httpHeaders, 
            HttpStatus.OK
        );

    }

}
