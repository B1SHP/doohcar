package bps.doohcar.controller;

import bps.doohcar.repositories.redis.TempoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import bps.doohcar.dtos.nominatim.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    private String weatherKey;

    @Value("${key}")
    private String key;

    @Autowired
    private TempoRepository tempoRespository;

    private static final String urlWeatherApi = "https://api.weatherapi.com/v1/forecast.json?lang=pt";
    private static final String urlNominatim = "https://nominatim.openstreetmap.org/reverse?lat=%s&lon=%s&format=json&zoom=10";
    private static final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/coleta")
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
    public ResponseEntity<Object> coleta(@RequestBody ColetaTempoRequest request, @RequestHeader("key") String key){

        if(key == null || !Pattern.matches(this.key, key)){

            return ResponseObject.error("Chave não autorizada ou null", HttpStatus.UNAUTHORIZED);

        }

        ResponseEntity<Object> validate = request.validate();

        if(validate != null){

            return validate;

        }

        try {

            String url = String.format(
                urlWeatherApi + "&key=%s&q=%s,%s", 
                weatherKey,
                request.latitude(),
                request.longitude()
            );

            Address address = restTemplate.getForObject(String.format(urlNominatim, request.latitude(), request.longitude()), Address.class);

            TempoDto tempoDto = tempoRespository.collectCache(address.displayName());

            if(tempoDto == null){

                try {

                    tempoDto = restTemplate.getForObject(url, TempoDto.class);
                    
                } catch (Exception e) {

                    return ResponseObject.error(
                        "A api de tempo não encontrou nenhum local com esta lat/lng", 
                        HttpStatus.NOT_FOUND
                    );

                }

                tempoRespository.createCache(address.displayName(), tempoDto);

            }

            List<TemperaturaDto> temperaturas = new ArrayList<>();

            for (HourDto hourDto : tempoDto.forecast().forecastday().get(0).hours()) {

                temperaturas.add(
                    new TemperaturaDto(
                        hourDto.hour(),
                        tempoDto.forecast().forecastday().get(0).dayDto().minTempC(), 
                        tempoDto.forecast().forecastday().get(0).dayDto().maxTempC(), 
                        hourDto.tempC(),
                        hourDto.condition().descricao(),
                        hourDto.condition().code(),
                        hourDto.windKpm(),
                        hourDto.humidity()
                    )
                );
                
            }
  
            return ColetaTempoResponse.success(
                tempoDto.location().name(), 
                temperaturas,
                tempoDto.forecast().forecastday().getFirst().astroDto().sunrise(),
                tempoDto.forecast().forecastday().getFirst().astroDto().sunset()
            );

        } catch (JsonProcessingException e) {

            return ResponseObject.error(
                "O servidor do redis esta com algum problema", 
                HttpStatus.INTERNAL_SERVER_ERROR
            );

		}

    }
    
}
