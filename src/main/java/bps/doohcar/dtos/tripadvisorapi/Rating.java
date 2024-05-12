package bps.doohcar.dtos.tripadvisorapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Rating(
    @JsonProperty("localized_name" )String localizedName,
    @JsonProperty("value") float value
) {}
