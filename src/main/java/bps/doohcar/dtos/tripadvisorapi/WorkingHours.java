package bps.doohcar.dtos.tripadvisorapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class WorkingHours {

    @JsonProperty("day") private int day;
    @JsonProperty("time") private String time;

    public String time(){

        return time.substring(0, 1) + ":" + time.substring(2);

    }

    public int day(){

        return day;

    }
    
}
