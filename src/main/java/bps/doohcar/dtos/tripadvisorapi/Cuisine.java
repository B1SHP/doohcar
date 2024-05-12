package bps.doohcar.dtos.tripadvisorapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Cuisine(
    @JsonProperty("localized_name") String localizedName
) {}
