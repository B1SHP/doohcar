package bps.doohcar.dtos.locais.requests;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

import bps.doohcar.dtos.ResponseObject;
import bps.doohcar.dtos.locais.PeriodoDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "coleta_restaurantes_request"
)
public record ColetaLocaisRequest(
    
    @Schema(
        nullable = true,
        description = "null: todos, 1: Restaurantes, 2: Atrações"
    )
    @JsonProperty("tipo") Integer tipo,

    @Schema(
        nullable = true,
        example = "[\"Bar\", \"Churrasco\"]",
        description = "Pode ser o nome do lugar ou um dos tipos de comida, caso não queira utilizar mande NULL, não vazio, NULL"
    )
    @JsonProperty("query") List<String> query,

    @Schema(
        nullable = false,
        example = "12"
    )
    @JsonProperty("limit") Integer limit,

    @Schema(
        nullable = false,
        example = "0"
    )
    @JsonProperty("offset") Integer offset,

    @Schema(
        nullable = false,
        example = "12"
    )
    @JsonProperty("quantidade_reviews") Integer quantidadeReviews,

    @Schema(
        nullable = true
    )
    @JsonProperty("periodo") PeriodoDto periodoDto

) {

    public ResponseEntity<Object> validate(){

        String message = null;

        if(tipo != null){

            if(tipo < 1 || tipo > 2){

                message = "A variavel 'tipo' só pode ser null, 1 ou 2";

            }

        }

        if(limit == null){

            message = "A variavel 'limit' não pode ser null";

        }

        if(offset == null){

            message = "A variavel 'offset' não pode ser null";

        }

        if(quantidadeReviews == null){

            message = "A variavel 'quantidade_reviews' não pode ser null";

        }

        if(periodoDto != null){

            ResponseEntity<Object> validate = periodoDto.validate();

            if(validate != null){

                return validate;

            }

        }

        return message == null ? null : ResponseObject.error(message, HttpStatus.BAD_REQUEST);

    }

}
