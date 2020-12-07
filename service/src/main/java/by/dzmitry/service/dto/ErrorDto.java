package by.dzmitry.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDto {

    private String message;

    public ErrorDto(String message) {
        this.message = message;
    }
}
