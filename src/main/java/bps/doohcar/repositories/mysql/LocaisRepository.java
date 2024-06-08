package bps.doohcar.repositories.mysql;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bps.doohcar.dtos.locais.requests.ColetaLocaisRequest;
import bps.doohcar.dtos.locais.responses.Local;
import bps.doohcar.repositories.mysql.rowmappers.LocalRowmapper;
import bps.doohcar.utlis.MysqlUtils;

@Repository
public class LocaisRepository {

    @Autowired
    private DataSource dataSource;

    private NamedParameterJdbcTemplate jdbcTemplate;

    public void aumentaContagem(long id){

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = """
            UPDATE 
                place 
            SET 
                contagem = contagem + 1
            WHERE 
                id = :id
        """;

        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("id", id);

        jdbcTemplate.update(sql, map);

    }

    public String coletaUrl(long id){

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = String.format("""
            SELECT 
                url 
            FROM 
                place 
            WHERE 
                id = %s
        """, id);

        try {

            return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), String.class);
            
        } catch (EmptyResultDataAccessException e) {

            return null;

        }

    }

    public long contaLocais(ColetaLocaisRequest request){

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = String.format("""
            SELECT 
                COUNT(id)
            FROM 
                place
            WHERE 
                %s
        """, 
            MysqlUtils.modificaColetaLocaisRequest(request)
        );

        try {

            return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Long.class);
            
        } catch (EmptyResultDataAccessException e) {

            return 0;

        }

    }

    public List<Local> coletaLocais(ColetaLocaisRequest request){

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = String.format("""
            SELECT 
                id,
                nome,
                url,
                telefone,
                notas_quantidade,
                nota,
                dias,
                fotos,
                cozinha,
                tipo,
                contagem
            FROM 
                place
            WHERE 
                %s
            LIMIT %d
            OFFSET %d
        """, 
            MysqlUtils.modificaColetaLocaisRequest(request),
            request.limit(),
            request.offset()
        );

        System.out.println(sql);

        try {

            return jdbcTemplate.query(sql, new LocalRowmapper());
            
        } catch (EmptyResultDataAccessException e) {

            return null;

        }

    }

    public Long coleta(String id){

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource)   ;

        String sql = """
            SELECT id
            FROM place
            WHERE id = :id
        """;

        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("id", id);

        try {

            return jdbcTemplate.queryForObject(sql, map, Long.class);
            
        } catch (Exception e) {

            return null;

        }

    }

    public void adicionaLocais(Local local, int tipo) throws JsonProcessingException{

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = """
            INSERT INTO place (id, nome, url, telefone, notas_quantidade, nota, dias, fotos, cozinha, tipo)
            VALUES(
        """;

        sql += local.id() + ", "; 
        sql += "'" + local.nome().replaceAll("'", "") + "'" + ", ";
        sql += "'" + local.urlSite() + "'" + ", ";
        sql += "'" + local.telefone() + "'" + ", ";
        sql += local.numeroDeNotas() + ", ";
        sql += "'" +new ObjectMapper().writeValueAsString(local.nota()) + "'" + ", "; 
        sql += "'" +new ObjectMapper().writeValueAsString(local.dias()) + "'" + ", ";
        sql += "'" +new ObjectMapper().writeValueAsString(local.fotos()) + "'" + ", ";
        sql += "'" +new ObjectMapper().writeValueAsString(local.cozinha()) + "'" + ", ";
        sql += tipo;
        sql += ");";

        jdbcTemplate.update(sql, new MapSqlParameterSource());

    }
    
}
