package bps.doohcar.dtos.tripadvisorapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class WorkingHours {

    @JsonProperty("day") private int day;
    @JsonProperty("time") private String time;

    public String day(){

        return switch(day){

            case 1 -> "Segunda-Feira";
            case 2 -> "Terça-Feira";
            case 3 -> "Quarta-Feira";
            case 4 -> "Quinta-Feira";
            case 5 -> "Sexta-Feira";
            case 6 -> "Sabado";
            case 7 -> "Domingo";
            default -> null;

        };

    }

    public String time(){
        return time.substring(0, 2) + ":" + time.substring(2);
    }
    
}
