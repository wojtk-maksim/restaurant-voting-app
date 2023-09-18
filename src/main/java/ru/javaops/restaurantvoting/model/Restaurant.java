package ru.javaops.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
public class Restaurant extends NamedDeletableEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Dish> dishes;

    public Restaurant(Long id, String name, boolean enabled, boolean deleted) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.deleted = deleted;
    }

    public Restaurant(Long id, String name, boolean enabled) {
        this(id, name, enabled, false);
    }

    public Restaurant(Long id, String name) {
        this(id, name, true);
    }

    public Restaurant(String name) {
        this(null, name);
    }

}
