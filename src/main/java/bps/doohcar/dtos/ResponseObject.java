package bps.doohcar.dtos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Schema(
    name = "response_object"
)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ResponseObject {

    @JsonProperty("success") private String success;
    @JsonProperty("error") private String error;

    public static ResponseEntity<ResponseObject> error(String error, HttpStatus httpStatus){

        return ResponseEntity
            .status(httpStatus)
            .body(new ResponseObject(null, error)
        );

    }

    public static ResponseEntity<ResponseObject> success(String success, HttpStatus httpStatus){

        return ResponseEntity
            .status(httpStatus)
            .body(new ResponseObject(success, null)
        );

    }
    
}
