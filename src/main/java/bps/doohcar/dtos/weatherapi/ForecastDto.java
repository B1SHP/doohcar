package bps.doohcar.dtos.weatherapi;

import java.util.List;

public record ForecastDto(
    List<ForecastDayDto> forecastday
) {}
