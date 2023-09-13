package ru.javaops.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.restaurantvoting.util.Views.Admin;
import ru.javaops.restaurantvoting.util.Views.Public;

import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
public class Restaurant extends NamedEntity implements Enablable, Deletable {

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    @JsonView(Public.class)
    protected boolean enabled = true;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Dish> dishes;
    @Column(name = "deleted", nullable = false, columnDefinition = "bool default false")
    @JsonView(Admin.class)
    private boolean deleted;

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
