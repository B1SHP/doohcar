package bps.doohcar.repositories.mysql.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import bps.doohcar.dtos.locais.Image;
import bps.doohcar.dtos.locais.responses.Dia;
import bps.doohcar.dtos.locais.responses.Local;
import bps.doohcar.dtos.locais.responses.Nota;

public class LocalRowmapper implements RowMapper<Local> {

	@Override
	public Local mapRow(ResultSet rs, int rowNum) throws SQLException {

        ObjectMapper objectMapper = new ObjectMapper();

        String nota = rs.getString("nota");
        String dias = rs.getString("dias");
        String fotos = rs.getString("fotos");
        String cozinha = rs.getString("cozinha");

        try {
			return new Local(
			    rs.getString("id"), 
			    rs.getString("nome"), 
			    rs.getString("url"), 
			    rs.getString("telefone"), 
			    rs.getLong("notas_quantidade"), 
			    objectMapper.readValue(nota, Nota.class), 
			    objectMapper.readValue(dias, new TypeReference<ArrayList<Dia>>(){}), 
			    objectMapper.readValue(fotos, new TypeReference<ArrayList<Image>>(){}), 
			    objectMapper.readValue(cozinha, new TypeReference<ArrayList<String>>(){}),
			    rs.getInt("tipo")
			);
		} catch (Exception e) {

            e.printStackTrace();

		}

        return null;

	}
    
}
