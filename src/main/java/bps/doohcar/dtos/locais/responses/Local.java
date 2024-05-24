package bps.doohcar.dtos.locais.responses;

import bps.doohcar.dtos.locais.Image;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record Local(

    @JsonProperty("id") String id,
    @JsonProperty("nome") String nome,
    @JsonProperty("url_site") String urlSite,
    @JsonProperty("telefone") String telefone,
    @JsonProperty("notas_quantidade") long numeroDeNotas,
    @JsonProperty("nota") Nota nota, //vira Json

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("dias") ArrayList<Dia> dias, // vira Json

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("fotos") ArrayList<Image> fotos, //vira Json

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("cozinha") ArrayList<String> cozinha,

    @JsonProperty("tipo") int tipo

) {}
