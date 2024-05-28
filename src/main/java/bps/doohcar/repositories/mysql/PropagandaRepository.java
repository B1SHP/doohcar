package bps.doohcar.repositories.mysql;

import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import bps.doohcar.dtos.propagandas.geral.PropagandaDto;
import bps.doohcar.dtos.propagandas.requests.CriaPropagandaRequest;

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

	public Long criaPropagada(CriaPropagandaRequest request) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'criaPropagada'");
	}

	public PropagandaDto coletaPropaganda(Long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'coletaPropaganda'");
	}

	public Collection<? extends PropagandaDto> coletaPropagandas(Long limit, Long offset) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'coletaPropagandas'");
	}

	public Long contaPropagandas() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'contaPropagandas'");
	}

	public void excluiPropaganda(Long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'excluiPropaganda'");
	}
    
}
