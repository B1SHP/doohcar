package bps.doohcar.dtos.diskingressos;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Hit(
    @JsonAlias("_index") String index,
    @JsonAlias("_type") String type,
    @JsonAlias("_id") String id,
    @JsonAlias("_source") Source source
) {
}
