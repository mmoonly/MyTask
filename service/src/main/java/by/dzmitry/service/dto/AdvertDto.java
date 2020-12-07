package by.dzmitry.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdvertDto extends EntityDto {

    private String heading;

    private LocalDate publicationDate;

    private Integer price;

    private Boolean active;

    private Boolean paid;

    private LocalDate endPaidDate;

    private String description;

}
