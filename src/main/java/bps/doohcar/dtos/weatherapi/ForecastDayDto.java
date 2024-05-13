package bps.doohcar.dtos.weatherapi;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public record ForecastDayDto(

    @JsonSerialize(using =  LocalDateSerializer.class) 
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date,

    @JsonProperty("day") DayDto dayDto,
    @JsonProperty("astro") AstroDto astroDto,
    @JsonProperty("hour") List<HourDto> hours

) {}
