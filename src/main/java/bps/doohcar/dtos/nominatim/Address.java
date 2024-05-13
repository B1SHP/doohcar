package bps.doohcar.dtos.nominatim;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Address(
    @JsonProperty("display_name") String displayName
) {}
