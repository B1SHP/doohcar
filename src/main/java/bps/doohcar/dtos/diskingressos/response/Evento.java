package bps.doohcar.dtos.diskingressos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record Evento(

    String id,
    String data,
    String nome,
    String foto,
    String url,
    String estado,
    String cidade,
    String local

) {
}
