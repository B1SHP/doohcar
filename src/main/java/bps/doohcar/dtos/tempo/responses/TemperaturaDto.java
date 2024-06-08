package bps.doohcar.dtos.tempo.responses;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "temperatura"
)
public record TemperaturaDto(
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonProperty("data_hora") LocalDateTime dataHora,
    @JsonProperty("minima") float minima,
    @JsonProperty("maxima") float maxima,
    @JsonProperty("atual") float media,
    @JsonProperty("tempo") String tempo,
    @JsonProperty("codigo") long codigo,
    @JsonProperty("velocidade_vento") float velocidadeVento,
    @JsonProperty("umidade") int umidade
) {}
