package bps.doohcar.dtos.weatherapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AstroDto(
    @JsonProperty("sunrise") String sunrise,
    @JsonProperty("sunset") String sunset,
    @JsonProperty("moonrise") String moonrise,
    @JsonProperty("moonset") String moonset
) {}
