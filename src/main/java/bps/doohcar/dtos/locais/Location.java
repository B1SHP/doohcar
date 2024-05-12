package bps.doohcar.dtos.locais;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Location(
    @JsonProperty("location_id") String locationId
) {}
