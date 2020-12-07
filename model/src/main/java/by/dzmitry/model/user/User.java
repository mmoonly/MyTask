package by.dzmitry.model.user;

import by.dzmitry.model.AEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@NoArgsConstructor
public class User extends AEntity {

    @Column(name = "login")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private Role role;

}
