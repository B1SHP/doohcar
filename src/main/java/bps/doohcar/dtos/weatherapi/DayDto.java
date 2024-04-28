package bps.doohcar.dtos.weatherapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DayDto(
    @JsonProperty("maxtemp_c") float maxTempC,
    @JsonProperty("mintemp_c") float minTempC,
    @JsonProperty("avgtemp_c") float avgTempC,
    @JsonProperty("maxwind_kph") float maxWindKph,
    @JsonProperty("avghumidity") int avgHumidity,
    @JsonProperty("daily_chance_of_rain") int dailyChanceOfRain
) {}
