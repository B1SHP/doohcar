package bps.doohcar.repositories.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bps.doohcar.dtos.weatherapi.TempoDto;

@Repository
public class TempoRepository {

    @Autowired
    @Qualifier("tempo")
    private RedisTemplate<String, String> redisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void createCache(String chave, TempoDto tempoDto) throws JsonProcessingException{

        redisTemplate
            .opsForValue()
            .set(
                chave, 
                objectMapper.writeValueAsString(tempoDto), Duration.ofSeconds(86400)
            )
        ;

    }

    public TempoDto collectCache(String chave) throws JsonMappingException, JsonProcessingException{

        String valor = redisTemplate.opsForValue().get(chave);

        return valor == null ? null : objectMapper.readValue(valor, TempoDto.class);

    }
    
}
