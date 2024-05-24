package bps.doohcar.dtos.locais.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record Nota(
    @JsonProperty("geral") float notaGeral,
    @JsonProperty("comida") Float comida,
    @JsonProperty("atendimento") Float atendimento,
    @JsonProperty("preco") Float preco
) {}
