package bps.doohcar.dtos.locais;

import java.time.LocalTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import bps.doohcar.dtos.ResponseObject;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "periodo"
)
public record PeriodoDto(

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(pattern = "HH:mm:ss")
    @Schema(
        example = "hh:mm:ss"
    )
    @JsonProperty("inicio") LocalTime inicio,

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @Schema(
        example = "hh:mm:ss"
    )
    @JsonFormat(pattern = "HH:mm:ss")
    @JsonProperty("fim") LocalTime fim

) {

    public ResponseEntity<Object> validate(){

        String message = null;

        if(inicio == null){

            message = "A variavel 'periodo.inicio' não pode ser null";

        }

        if(fim == null){

            message = "A variavel 'periodo.fim' não pode ser null";

        }

        return message == null ? null : ResponseObject.error(message, HttpStatus.BAD_REQUEST); 

    }

}
