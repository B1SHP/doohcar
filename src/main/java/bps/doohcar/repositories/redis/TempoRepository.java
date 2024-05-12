package bps.doohcar.repositories.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TempoRepository {

    @Autowired
    @Qualifier("tempo")
    private RedisTemplate<String, String> redisTemplate;

    public void createCache(long id){

    }
    
}
