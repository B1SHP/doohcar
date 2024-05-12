package bps.doohcar.dtos.locais;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Image(

    @JsonProperty("thumbnail") Format thumbnail,
    @JsonProperty("small") Format small,
    @JsonProperty("medium") Format medium,
    @JsonProperty("large") Format large,
    @JsonProperty("original") Format original
    
) {}
