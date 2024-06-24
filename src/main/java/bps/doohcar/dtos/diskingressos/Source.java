package bps.doohcar.dtos.diskingressos;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Source(
    Integer id,
    String date,
    @JsonAlias("eventname" )String eventName,
    String image,
    String state,
    String city,
    @JsonAlias("groupid")Integer groupId
) {
}
