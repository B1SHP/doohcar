package bps.doohcar.dtos.tripadvisorapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Period(

    @JsonProperty("open") WorkingHours open,
    @JsonProperty("close") WorkingHours close

) {}
