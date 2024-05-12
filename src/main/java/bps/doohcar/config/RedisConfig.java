package bps.doohcar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    private RedisConnectionFactory databaseConnection(int database){

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(host, port);
        lettuceConnectionFactory.setDatabase(database);
        lettuceConnectionFactory.start();
        return lettuceConnectionFactory;

    }

    @Bean(name = "tempo")
    public RedisTemplate<String, String> redisTemplateDb0() {
        return new StringRedisTemplate(
            databaseConnection(0)
        );
    }

    @Bean(name = "locais")
    public RedisTemplate<String, String> redisTemplateDb1() {
        return new StringRedisTemplate(
            databaseConnection(1)
        );
    }

    
}
