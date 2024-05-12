package bps.doohcar.dtos.tripadvisorapi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "detalhe"
)
public record DetailDto(

    @JsonProperty("name") String name,
    @JsonProperty("web_url") String webUrl,
    @JsonProperty("address_obj") AddressObj addressObj,
    @JsonProperty("phone") String phone,
    @JsonProperty("rating") float rating,
    @JsonProperty("num_reviews") int numReviews,
    @JsonProperty("subratings") SubRatings subRating,
    @JsonProperty("hours") Hours hours,
    @JsonProperty("cuisine") List<Cuisine> cuisines

) {}
