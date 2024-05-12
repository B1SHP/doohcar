package bps.doohcar.controller;

import java.time.LocalTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import bps.doohcar.dtos.ResponseObject;
import bps.doohcar.dtos.tempo.requests.ColetaTempoRequest;
import bps.doohcar.dtos.tempo.responses.ColetaTempoResponse;
import bps.doohcar.dtos.tempo.responses.TemperaturaDto;
import bps.doohcar.dtos.weatherapi.HourDto;
import bps.doohcar.dtos.weatherapi.TempoDto;

@RestController
@RequestMapping("/api/v1/dooh-car/tempo")
public class TempoController {

    @Value("${weather.api.key}")
    private String key;

    private static final String urlWeatherApi = "https://api.weatherapi.com/v1/forecast.json?lang=pt";

    private static final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/coleta")
    public ResponseEntity<Object> coleta(@RequestBody ColetaTempoRequest request){

        String url = String.format(
            urlWeatherApi + "&key=%s&q=%s,%s", 
            key,
            request.latitude(),
            request.longitude()
        );

        TempoDto tempoDto = restTemplate.getForObject(url, TempoDto.class);

        int hora = LocalTime.now(ZoneId.of("America/Sao_Paulo")).getHour();

        for (HourDto hourDto : tempoDto.forecast().forecastday().get(0).hours()) {

            System.out.println("@==================================================@");
            System.out.println(hourDto.toString());

            if(hora == hourDto.hour().getHour()){

                return ColetaTempoResponse.success(
                    tempoDto.location().name(), 
                    new TemperaturaDto(
                        tempoDto.forecast().forecastday().get(0).dayDto().minTempC(), 
                        tempoDto.forecast().forecastday().get(0).dayDto().maxTempC(), 
                        hourDto.tempC(),
                        hourDto.condition().descricao()
                    )
                );

            }
            
        }

        return ResponseObject.error(
            "Não foi possivel encontrar a hora atual", 
            HttpStatus.INTERNAL_SERVER_ERROR
        );

    }
    
}
