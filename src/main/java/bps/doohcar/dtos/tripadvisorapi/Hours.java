package bps.doohcar.dtos.tripadvisorapi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Hours(

    @JsonProperty("periods") List<Period> periods

) {}
