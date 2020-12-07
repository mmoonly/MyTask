package by.dzmitry.model.comment;

import by.dzmitry.model.AEntity;
import by.dzmitry.model.advert.Advert;
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
@Table(name = "comments")
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@NoArgsConstructor
public class Comment extends AEntity {

    @Column(name = "value")
    private String value;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "advert_id", nullable = false)
    private Advert advert;

    public Comment(String value, LocalDate now, Profile profile, Advert advert) {
        this.value = value;
        this.publicationDate = now;
        this.profile = profile;
        this.advert = advert;
    }

    public Comment(Integer id, String value, LocalDate date, Profile profile, Advert advert) {
        super.setId(id);
        this.value = value;
        this.publicationDate = date;
        this.profile = profile;
        this.advert = advert;
    }
}
