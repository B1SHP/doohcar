package bps.doohcar.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import bps.doohcar.dtos.ResponseObject;
import bps.doohcar.dtos.locais.DataLocation;
import bps.doohcar.dtos.locais.DataPhotos;
import bps.doohcar.dtos.locais.Location;
import bps.doohcar.dtos.locais.requests.ColetaRestaurantesRequest;
import bps.doohcar.dtos.tripadvisorapi.DetailDto;

import static bps.doohcar.utlis.ChamadaUtils.chamadaLocation;

@RestController
@RequestMapping("/api/v1/dooh-car/locais")
public class LocaisController {

    private String locationUrl = "https://api.content.tripadvisor.com/api/v1/location/search?searchQuery=restaurantes&category=restaurants&radiusUnit=km&language=pt";

    private String detailsUrl = "https://api.content.tripadvisor.com/api/v1/location/%s/details?language=pt&currency=BRL&key=%s";

    private String photosUrl = "https://api.content.tripadvisor.com/api/v1/location/%s/photos?language=pt&limit=5&offset=0&source=Management&key=%s";

    @Value("${trip-advisor.api.key}")
    private String key;

    @GetMapping("/restaurantes")
    public ResponseEntity<Object> coletaRestaurantes(@RequestBody ColetaRestaurantesRequest request){

        ResponseEntity<Object> validate = request.validate();

        if(validate != null){

            return validate;

        }

        ResponseEntity<DataLocation> data = chamadaLocation(locationUrl, DataLocation.class, 
            "key", key,
            "latLong", (request.latitude() + "," + request.longitude())
        );


        Location location = data.getBody().locations().getFirst();

        String url = String.format(detailsUrl, location.locationId(), key);

//                ResponseEntity<DetailDto> detail = new RestTemplate().getForEntity(url, DetailDto.class);

        url = String.format(photosUrl, location.locationId(), key);

        System.out.println(url);

        ResponseEntity<DataPhotos> dataPhotos = new RestTemplate().getForEntity(url, DataPhotos.class);

        System.out.println("Data: " + dataPhotos.getBody().toString());


        return ResponseObject.success(data.getBody().toString(), HttpStatus.OK);

    }
    
}
