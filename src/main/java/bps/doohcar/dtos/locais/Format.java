package bps.doohcar.dtos.locais;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Format(
    @JsonProperty("url") String url
) {}
