package bps.doohcar.dtos.tempo.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

import bps.doohcar.dtos.ResponseObject;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "coleta_tempo_response"
)
public class ColetaTempoResponse extends ResponseObject {

    @JsonProperty("cidade") 
    private String cidade;

    @JsonProperty("temperatura") 
    private TemperaturaDto temperaturaDto;

    private ColetaTempoResponse(String cidade, TemperaturaDto temperaturaDto){

        this.cidade = cidade;
        this.temperaturaDto = temperaturaDto;
        super.setSuccess("Tempo encontrado com sucesso");

    }

    public static ResponseEntity<Object> success(String cidade, TemperaturaDto temperaturaDto){

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ColetaTempoResponse(cidade, temperaturaDto)
        );

    }

}
