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

    public boolean verificaSeEPatrocinado(String idDisk){

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = String.format("""
            SELECT COUNT(id)
            FROM eventos
            WHERE id = '%s'
            AND patrocinado = 1
        """, idDisk);

        try {

            Long quantidade = jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Long.class);

            if(quantidade == null || quantidade < 1){

                return false;

            }

            return true;
            
        } catch (EmptyResultDataAccessException e) {

            return false;

        }

    }

    public boolean verificaSeOEventoJaExiste(String idDisk){

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = String.format("""
            SELECT COUNT(id)
            FROM eventos
            WHERE id = '%s'
        """, idDisk);

        try {

            Long quantidade = jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Long.class);

            if(quantidade == null || quantidade < 1){

                return false;

            }

            return true;
            
        } catch (EmptyResultDataAccessException e) {

            return false;

        }

    }

    public void cria(String idDisk, String nome){

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String nomeLimpo = nome.replaceAll("'", "");

        String sql = String.format("""
            INSERT INTO eventos(id, nome)
            VALUES('%s', '%s')
        """, idDisk, nomeLimpo);

        jdbcTemplate.update(sql, new MapSqlParameterSource());

    } 

    public void aumentaContagem(String id){

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = String.format("""
            UPDATE 
                eventos
            SET 
                contagem = contagem + 1
            WHERE 
                id = '%s'
        """, id);

        MapSqlParameterSource map = new MapSqlParameterSource();

        jdbcTemplate.update(sql, map);

    }

	public void alteraPatrocinio(String id, Integer patrocinado) {

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = String.format("""
            UPDATE 
                eventos
            SET 
                patrocinado = %d
            WHERE 
                id = '%s'
        """, patrocinado, id);

        MapSqlParameterSource map = new MapSqlParameterSource();

        jdbcTemplate.update(sql, map);

	}
    
}
