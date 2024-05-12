package bps.doohcar.dtos.locais;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageObject(

    @JsonProperty("id") long id,
    @JsonProperty("images") Image image
    
) {}
