package bps.doohcar.dtos.locais;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DataPhotos(
    @JsonProperty("data") List<ImageObject> imageObject
) {}
