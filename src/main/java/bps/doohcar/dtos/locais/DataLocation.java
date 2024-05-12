package bps.doohcar.dtos.locais;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DataLocation (
    @JsonProperty("data") List<Location> locations
) {}
