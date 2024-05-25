package bps.doohcar.repositories.mysql;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PropagandaRepository {

    @Autowired
    private DataSource dataSource;
    private NamedParameterJdbcTemplate jdbcTemplate;

    public String coletaUrl(long id){

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = String.format("""
            SELECT 
                url
            FROM 
                propaganda
            WHERE 
                id = %d
        """, id);

        try {

            return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), String.class);
            
        } catch (EmptyResultDataAccessException e) {

            return null;

        }

    }
    
}
