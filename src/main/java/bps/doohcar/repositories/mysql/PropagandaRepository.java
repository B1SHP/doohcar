package bps.doohcar.repositories.mysql;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import bps.doohcar.dtos.propagandas.geral.PropagandaDto;
import bps.doohcar.dtos.propagandas.requests.AlteraPropagandaRequest;
import bps.doohcar.dtos.propagandas.requests.CriaPropagandaRequest;
import bps.doohcar.repositories.mysql.rowmappers.PropagandaRowmapper;
import bps.doohcar.utlis.MysqlUtils;

@Repository
public class PropagandaRepository {

    @Autowired
    private DataSource dataSource;
    private NamedParameterJdbcTemplate jdbcTemplate;

    public void aumentaContagem(long id){

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = """
            UPDATE 
                propagandas
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
                url_redirecionamento
            FROM 
                propagandas
            WHERE 
                id = %d
                AND excluido IS NULL
        """, id);

        try {

            return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), String.class);
            
        } catch (EmptyResultDataAccessException e) {

            return null;

        }

    }

	public Long criaPropagada(CriaPropagandaRequest request, String urlImagem) {

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = String.format("""
            INSERT INTO propagandas(titulo, url_video, url_imagem, url_redirecionamento, tela_de_display)
            VALUES(%s, %s, %s, %s, %d)
        """,
            ("'" + request.titulo() + "'"),
            ("'" + request.urlVideo() + "'"),
            ("'" + urlImagem + "'"),
            ("'" + request.urlRedirecionamento() + "'"),
            request.telaDeDisplay()
        );

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, new MapSqlParameterSource(), keyHolder);

        try {

            return keyHolder.getKey().longValue();
            
        } catch (Exception e) {

            return null;

        }

	}

	public PropagandaDto coletaPropaganda(Long id) {

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = """
            SELECT 
                id,
                titulo,
                url_video,
                url_imagem,
                url_redirecionamento,
                contagem,
                tela_de_display
            FROM 
                propagandas
            WHERE 
                id = :id
                AND excluido IS NULL
        """;

        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("id", id);

        try {

            return jdbcTemplate.queryForObject(sql, map, new PropagandaRowmapper());
            
        } catch (EmptyResultDataAccessException e) {

            return null;

        }
		
    }

	public List<PropagandaDto> coletaPropagandas(Long limit, Long offset, String key) {

		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = """
            SELECT 
                id,
                titulo,
                url_video,
                url_imagem,
                url_redirecionamento,
                contagem,
                tela_de_display
            FROM 
                propagandas
            WHERE 
                excluido IS NULL
            LIMIT 
                :limit 
            OFFSET 
                :offset
        """;

        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("limit", limit);
        map.addValue("offset", offset);

        try {

            return jdbcTemplate.query(sql, map, new PropagandaRowmapper(key));
            
        } catch (EmptyResultDataAccessException e) {

            return null;

        }	

    }

	public Long contaPropagandas() {

		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = """
            SELECT 
                COUNT(id)
            FROM 
                propagandas
            WHERE 
                excluido IS NULL
        """;

        MapSqlParameterSource map = new MapSqlParameterSource();

        try {

            return jdbcTemplate.queryForObject(sql, map, Long.class);
            
        } catch (EmptyResultDataAccessException e) {

            return null;

        }	

	}

	public void excluiPropaganda(Long id) {

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = """
            UPDATE 
                propagandas
            SET 
                excluido = UTC_TIMESTAMP()
            WHERE 
                id = :id
        """;

        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("id", id);

        jdbcTemplate.update(sql, map);

	}

	public void alteraAnuncio(AlteraPropagandaRequest request, String urlImagem) {

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = String.format("""
            UPDATE 
                propagandas
            SET 
                %s
            WHERE 
                id = :id
        """, MysqlUtils.alteraPropaganda(request, urlImagem));

        MapSqlParameterSource map = new MapSqlParameterSource();

        System.out.println(sql);

        map.addValue("id", request.id());

        jdbcTemplate.update(sql, map);

	}

	public boolean verificaSeOLocalDaImagemEstaDisponivel(Integer telaDeDisplay) {

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = """
            SELECT 
                COUNT(id)
            FROM 
                propagandas
            WHERE 
                tela_de_display = :telaDeDisplay
                AND excluido IS NULL
        """;

        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("telaDeDisplay", telaDeDisplay);

        try {

            Integer numeroDeImagemsNaTelaEscolhida = jdbcTemplate.queryForObject(sql, map, Integer.class);

            if(numeroDeImagemsNaTelaEscolhida == null || numeroDeImagemsNaTelaEscolhida > 0){

                System.out.println("return");

                return false;

            };

            return true;
            
        } catch (EmptyResultDataAccessException e) {

            return false;

        }

	}

	public PropagandaDto coletaPropaganda(Long id, String key) {

		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String sql = """
            SELECT 
                id,
                titulo,
                url_video,
                url_imagem,
                url_redirecionamento,
                contagem,
                tela_de_display
            FROM 
                propagandas
            WHERE 
                id = :id
                AND excluido IS NULL
        """;

        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("id", id);

        try {

            return jdbcTemplate.queryForObject(sql, map, new PropagandaRowmapper());
            
        } catch (EmptyResultDataAccessException e) {

            return null;

        }	

    }
    
}
