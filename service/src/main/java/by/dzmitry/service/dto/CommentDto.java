package by.dzmitry.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentDto extends EntityDto {

    private String value;

    private LocalDate publicationDate;

    private String name;
}
