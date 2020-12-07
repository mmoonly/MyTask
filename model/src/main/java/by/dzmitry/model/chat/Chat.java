package by.dzmitry.model.chat;

import by.dzmitry.model.AEntity;
import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.message.Message;
import by.dzmitry.model.profile.Profile;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "chats")
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@NoArgsConstructor
public class Chat extends AEntity {

    @ManyToOne
    @JoinColumn(name = "sender_profile_id", nullable = false)
    private Profile senderProfile;

    @ManyToOne
    @JoinColumn(name = "advert_id", nullable = false)
    private Advert advert;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

    public Chat(Profile senderProfile, Advert advert) {
        this.senderProfile = senderProfile;
        this.advert = advert;
    }

    public Chat(Integer id, Profile senderProfile, Advert advert) {
        super.setId(id);
        this.senderProfile = senderProfile;
        this.advert = advert;
    }
}
