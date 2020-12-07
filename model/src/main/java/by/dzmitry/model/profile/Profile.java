package by.dzmitry.model.profile;

import by.dzmitry.model.AEntity;
import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.chat.Chat;
import by.dzmitry.model.comment.Comment;
import by.dzmitry.model.message.Message;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "profiles")
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@NoArgsConstructor
public class Profile extends AEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "rating")
    private Integer rating;

    @OneToMany(mappedBy = "profile")
    private List<Advert> adverts;

    @OneToMany(mappedBy = "profile")
    private List<Comment> comments;

    @OneToMany(mappedBy = "profile")
    private List<Message> messages;

    @OneToMany(mappedBy = "senderProfile")
    private List<Chat> chats;

    public Profile(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.rating = 0;
    }

    public Profile(Integer id, String name, String surname, Integer rating) {
        super.setId(id);
        this.name = name;
        this.surname = surname;
        this.rating = rating;
    }
}
