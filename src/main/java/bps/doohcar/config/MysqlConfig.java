package bps.doohcar.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MysqlConfig {

    @Value("${mysql.url}")
    private String geralUrl;

    @Value("${mysql.username}")
    private String username;

    @Value("${mysql.password}")
    private String password;

    @Bean(name = "geral")
    public DataSource dataSourgeGeral(){

        return DataSourceBuilder
            .create()
            .url(
                geralUrl
            )
            .username(
                username
            )
            .password(
                password
            )
            .build()
        ;

    }
    
    @Bean
    @Primary
    public DataSource geralUrl(){

        return DataSourceBuilder
            .create()
            .url(
                geralUrl
            )
            .username(
                username
            )
            .password(
                password
            )
            .build()
        ;

    }
}
