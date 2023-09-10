package ru.javaops.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.restaurantvoting.util.Views.Admin;

import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
public class Restaurant extends NamedEntity implements Enableable {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Dish> dishes;

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    @JsonView(Admin.class)
    private boolean enabled = true;

    public Restaurant(Long id, String name, boolean enabled) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
    }

    public Restaurant(Long id, String name) {
        this(id, name, true);
    }

    public Restaurant(String name) {
        this(null, name);
    }
}
