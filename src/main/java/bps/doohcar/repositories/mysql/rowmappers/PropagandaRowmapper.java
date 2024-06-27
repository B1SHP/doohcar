package bps.doohcar.repositories.mysql.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import bps.doohcar.dtos.propagandas.geral.PropagandaDto;

public class PropagandaRowmapper implements RowMapper<PropagandaDto>{

    private String key;

    public PropagandaRowmapper(){}

    public PropagandaRowmapper(String key){

        this.key = key;

    }

	@Override
	public PropagandaDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        String urlRedirecionamento = rs.getString("url_redirecionamento");

        long id = rs.getLong("id");

        if(key != null){

            urlRedirecionamento = "http://54.207.32.232:8080/api/v1/dooh-car/redirect/redireciona?tipo=1&id=" + id + "&key=" + key; 

        }

        return new PropagandaDto(
            id,
            rs.getString("titulo"), 
            rs.getString("url_video"), 
            rs.getString("url_imagem"), 
            urlRedirecionamento,
            rs.getLong("contagem"),
            rs.getInt("tela_de_display")
        );

	}
    
}
