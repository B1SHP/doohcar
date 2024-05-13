package bps.doohcar.controller;

import bps.doohcar.repositories.redis.TempoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import bps.doohcar.dtos.nominatim.Address;

import java.time.LocalTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import bps.doohcar.dtos.ResponseObject;
import bps.doohcar.dtos.tempo.requests.ColetaTempoRequest;
import bps.doohcar.dtos.tempo.responses.ColetaTempoResponse;
import bps.doohcar.dtos.tempo.responses.TemperaturaDto;
import bps.doohcar.dtos.weatherapi.HourDto;
import bps.doohcar.dtos.weatherapi.TempoDto;

@RestController
@RequestMapping("/api/v1/dooh-car/tempo")
@Tag(
    name = "APIs DOOH-CAR: TEMPO",
    description = "CONTÉM TODOS OS ENDPOINTS RELACIONADOS AO TEMPO"
)
public class TempoController {

    @Value("${weather.api.key}")
    private String key;

    @Autowired
    private TempoRepository tempoRespository;

    private static final String urlWeatherApi = "https://api.weatherapi.com/v1/forecast.json?lang=pt";
    private static final String urlNominatim = "https://nominatim.openstreetmap.org/reverse?lat=%s&lon=%s&format=json&zoom=10";
    private static final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/coleta")
    @Operation(
        summary = "API UTILIZADA PARA A COLETA DO TEMPO",
        responses = {
            @ApiResponse(
                responseCode = "400",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "500",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = ColetaTempoResponse.class)
                )
            )
        }
    )
    public ResponseEntity<Object> coleta(@RequestBody ColetaTempoRequest request){

        ResponseEntity<Object> validate = request.validate();

        if(validate != null){

            return validate;

        }

        try {

            String url = String.format(
                urlWeatherApi + "&key=%s&q=%s,%s", 
                key,
                request.latitude(),
                request.longitude()
            );

            Address address = restTemplate.getForObject(String.format(urlNominatim, request.latitude(), request.longitude()), Address.class);

            TempoDto tempoDto = null;

            if(tempoDto == null){

                System.out.println("Url: " + url);

                try {

                    tempoDto = restTemplate.getForObject(url, TempoDto.class);
                    
                } catch (Exception e) {

                    return ResponseObject.error(
                        "A api de tempo não encontrou nenhum local com esta lat/lng", 
                        HttpStatus.NOT_FOUND
                    );

                }

                tempoRespository.createCache(address.displayName(), tempoDto);

                System.out.println("No cache");

            } else {System.out.println("Cache");}

            int hora = LocalTime.now(ZoneId.of("America/Sao_Paulo")).getHour();

            for (HourDto hourDto : tempoDto.forecast().forecastday().get(0).hours()) {

                if(hora == hourDto.hour().getHour()){

                    return ColetaTempoResponse.success(
                        tempoDto.location().name(), 
                        new TemperaturaDto(
                            tempoDto.forecast().forecastday().get(0).dayDto().minTempC(), 
                            tempoDto.forecast().forecastday().get(0).dayDto().maxTempC(), 
                            hourDto.tempC(),
                            hourDto.condition().descricao(),
                            hourDto.condition().code()
                        )
                    );

                }
                
            }

            return ResponseObject.error(
                "Não foi possivel encontrar a hora atual", 
                HttpStatus.INTERNAL_SERVER_ERROR
            );

        } catch (JsonProcessingException e) {

            return ResponseObject.error(
                "O servidor do redis esta com algum problema", 
                HttpStatus.INTERNAL_SERVER_ERROR
            );

		}

    }
    
}
