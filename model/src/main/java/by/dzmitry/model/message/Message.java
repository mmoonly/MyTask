package by.dzmitry.model.message;

import by.dzmitry.model.AEntity;
import by.dzmitry.model.chat.Chat;
import by.dzmitry.model.profile.Profile;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "messages")
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@NoArgsConstructor
public class Message extends AEntity {

    @Column(name = "value")
    private String value;

    @Column(name = "send_date")
    private LocalDate sendDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    public Message(String value, LocalDate now, Profile profile, Chat chat) {
        this.value = value;
        this.sendDate = now;
        this.profile = profile;
        this.chat = chat;
    }

    public Message(Integer id, String value, LocalDate date, Profile profile, Chat chat) {
        super.setId(id);
        this.value = value;
        this.sendDate = date;
        this.profile = profile;
        this.chat = chat;
    }
}
