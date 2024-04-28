package bps.doohcar.dtos.weatherapi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HourDto(
    @JsonProperty("time") String time,
    @JsonProperty("temp_c") float tempC,
    @JsonProperty("condition") ConditionDto condition,
    @JsonProperty("wind_kpm") float windKpm,
    @JsonProperty("humidity") int humidity
) {

    public LocalDateTime hour(){

        return LocalDateTime.parse(
            time,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );

    }

}
