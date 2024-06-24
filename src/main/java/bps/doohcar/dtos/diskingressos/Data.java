package bps.doohcar.dtos.diskingressos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Data(
    int total,
    @JsonAlias("max_score") float maxScore,
    @JsonAlias("hits") List<Hit> hits
) {
}
