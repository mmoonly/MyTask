package by.dzmitry.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProfileDto extends EntityDto {

    private String name;

    private String surname;

    private Integer rating;
}
