package bps.doohcar.dtos.propagandas.responses;

import bps.doohcar.dtos.propagandas.geral.PropagandaDto;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "coleta_propaganda_response"
)
@JsonInclude(Include.NON_NULL)
public record ColetaPropagandaResponse(

    @JsonProperty("propagandas") Map<Integer, PropagandaDto> propagandas,
    @JsonProperty("quantidade") Long quantidade

) {

    public static ResponseEntity<Object> answer(Map<Integer, PropagandaDto> propagandas, Long quantidade){

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ColetaPropagandaResponse(propagandas, quantidade))
        ;

    }

}
