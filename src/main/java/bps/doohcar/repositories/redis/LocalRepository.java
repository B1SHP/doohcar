package bps.doohcar.repositories.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bps.doohcar.dtos.locais.responses.Local;
@Repository
public class LocalRepository {

    @Autowired
    @Qualifier("local")
    private RedisTemplate<String, String> redisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void createCache(String chave, Local local) throws JsonProcessingException{

        redisTemplate
            .opsForValue()
            .set(
                chave, 
                objectMapper.writeValueAsString(local), Duration.ofSeconds(1296000)
            )
        ;

    }

    public Local collectCache(String chave) throws JsonMappingException, JsonProcessingException{

        String valor = redisTemplate.opsForValue().get(chave);

        return valor == null ? null : objectMapper.readValue(valor, Local.class);

    }
 
    
}
