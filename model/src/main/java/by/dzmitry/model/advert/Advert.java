package by.dzmitry.model.advert;

import by.dzmitry.model.AEntity;
import by.dzmitry.model.category.Category;
import by.dzmitry.model.chat.Chat;
import by.dzmitry.model.comment.Comment;
import by.dzmitry.model.profile.Profile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "adverts")
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@NoArgsConstructor
public class Advert extends AEntity {

    @Column(name = "heading")
    private String heading;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "price")
    private Integer price;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "end_paid_date")
    private LocalDate endPaidDate;

    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @OneToMany(mappedBy = "advert")
    private List<Comment> comments;

    @OneToMany(mappedBy = "advert")
    private List<Chat> chats;

    public Advert(String heading, LocalDate publicationDate, Integer price, String description, Category category, Profile profile) {
        this.heading = heading;
        this.publicationDate = publicationDate;
        this.price = price;
        this.description = description;
        this.category = category;
        this.profile = profile;
        this.active = true;
        this.paid = false;
    }

    public Advert(Integer id, String heading, LocalDate publicationDate, Integer price, String description, Category category, Profile profile) {
        super.setId(id);
        this.heading = heading;
        this.publicationDate = publicationDate;
        this.price = price;
        this.description = description;
        this.category = category;
        this.profile = profile;
        this.active = false;
        this.paid = false;
    }

    public Advert(Integer id, String heading, LocalDate publicationDate, Integer price, Boolean active, Boolean paid,
                  LocalDate endPaidDate, String description, Category category, Profile profile) {
        super.setId(id);
        this.heading = heading;
        this.publicationDate = publicationDate;
        this.price = price;
        this.active = active;
        this.paid = paid;
        this.endPaidDate = endPaidDate;
        this.description = description;
        this.category = category;
        this.profile = profile;
    }
}
