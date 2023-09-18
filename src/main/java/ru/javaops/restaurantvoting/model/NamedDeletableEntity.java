package ru.javaops.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import ru.javaops.restaurantvoting.util.Views;
import ru.javaops.restaurantvoting.util.Views.Public;

@MappedSuperclass
@Getter
@Setter
public abstract class NamedDeletableEntity extends EnablableEntity implements Deletable {

    @Column(name = "name", nullable = false)
    @JsonView(Public.class)
    protected String name;

    @Column(name = "deleted", nullable = false, columnDefinition = "bool default false")
    @JsonView(Views.Admin.class)
    protected boolean deleted;

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + id + " " + name;
    }

}
