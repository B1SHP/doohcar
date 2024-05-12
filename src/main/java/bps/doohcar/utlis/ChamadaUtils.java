package bps.doohcar.utlis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class ChamadaUtils {

    public static <T> ResponseEntity<T> chamadaLocation(String urlBasica, Class<T> clazz, String... params){

        String url = urlBasica;

        for(int i = 0 ; i < params.length ; i += 2){

            url += "&" + params[i] + "=" + params[i + 1];

        }

        return new RestTemplate().getForEntity(url, clazz);

    }
   
}
