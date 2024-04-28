package bps.doohcar.dtos.tempo.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "temperatura"
)
public record TemperaturaDto(
    @JsonProperty("minima") float minima,
    @JsonProperty("maxima") float maxima,
    @JsonProperty("atual") float media,
    @JsonProperty("tempo") String tempo
) {}
