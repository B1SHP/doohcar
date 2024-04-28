package bps.doohcar.dtos.weatherapi;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ForecastDayDto(
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date,
    @JsonProperty("day") DayDto dayDto,
    @JsonProperty("astro") AstroDto astroDto,
    @JsonProperty("hour") List<HourDto> hours
) {}
