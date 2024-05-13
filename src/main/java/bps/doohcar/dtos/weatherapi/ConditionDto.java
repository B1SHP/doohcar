package bps.doohcar.dtos.weatherapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ConditionDto(
    @JsonProperty("text") String descricao,
    @JsonProperty("code") long code
) {}
