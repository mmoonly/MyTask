package by.dzmitry.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class MessageDto extends EntityDto {

    private String value;

    private LocalDate sendDate;

    private String name;
}
