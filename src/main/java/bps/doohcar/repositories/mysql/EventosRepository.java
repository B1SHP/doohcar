package bps.doohcar.repositories.mysql;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EventosRepository {

    @Autowired
    private DataSource dataSource;
    private NamedParameterJdbcTemplate jdbcTemplate;

    public void cria(long id, String nome){

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = String.format("""
            INSERT INTO eventos(id, nome)
            VALUES(%d, %s)
        """, id, "'" + nome + "'");

        jdbcTemplate.update(sql, new MapSqlParameterSource());

    }

    public Long coleta(long id){

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = """
            SELECT 
                COUNT(id)
            FROM 
                eventos
            WHERE 
                id = :id
        """;

        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("id", id);

        try {

            return jdbcTemplate.queryForObject(sql, map, Long.class);
            
        } catch (EmptyResultDataAccessException e) {

            return 0l;

        }

    }

    public void aumentaContagem(long id){

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = """
            UPDATE 
                eventos
            SET 
                contagem = contagem + 1
            WHERE 
                id = :id
        """;

        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("id", id);

        jdbcTemplate.update(sql, map);

    }
    
}
