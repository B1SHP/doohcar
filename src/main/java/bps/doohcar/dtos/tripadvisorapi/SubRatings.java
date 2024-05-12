package bps.doohcar.dtos.tripadvisorapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SubRatings(

    @JsonProperty("0") Rating comida,
    @JsonProperty("1") Rating atendimento,
    @JsonProperty("2") Rating preco
    
) {}
