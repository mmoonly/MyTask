package by.dzmitry.model.category;

import by.dzmitry.model.AEntity;
import by.dzmitry.model.advert.Advert;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "categories")
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@NoArgsConstructor
public class Category extends AEntity {

    @Column(name = "thread")
    private String thread;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "category")
    private List<Advert> adverts;

    public Category(String thread) {
        this.thread = thread;
    }

    public Category(String thread, Category parent) {
        this.thread = thread;
        this.parent = parent;
    }

    public Category(Integer id, String thread) {
        super.setId(id);
        this.thread = thread;
    }

    public Category(Integer id, String thread, Category parentCategory) {
        super.setId(id);
        this.thread = thread;
        this.parent = parentCategory;
    }
}
