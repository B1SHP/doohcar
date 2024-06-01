package bps.doohcar.repositories.mysql.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import bps.doohcar.dtos.propagandas.geral.PropagandaDto;

public class PropagandaRowmapper implements RowMapper<PropagandaDto>{

	@Override
	public PropagandaDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new PropagandaDto(
            rs.getLong("id"), 
            rs.getString("titulo"), 
            rs.getString("url_video"), 
            rs.getString("url_imagem"), 
            rs.getString("url_redirecionamento"),
            rs.getLong("contagem")
        );

	}
    
}
