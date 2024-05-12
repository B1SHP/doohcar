package bps.doohcar.dtos.tripadvisorapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddressObj(

    @JsonProperty("street1") String street1,
    @JsonProperty("street2") String street2,
    @JsonProperty("city") String city,
    @JsonProperty("state") String state,
    @JsonProperty("country") String country,
    @JsonProperty("postalcode") String postalCode,
    @JsonProperty("address_string") String addressString
    
) {}
