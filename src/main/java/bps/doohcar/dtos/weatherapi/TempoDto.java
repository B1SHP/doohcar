package bps.doohcar.dtos.weatherapi;

public record TempoDto(
    LocationDto location,
    ForecastDto forecast
) {}
