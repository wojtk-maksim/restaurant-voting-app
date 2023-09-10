package ru.javaops.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import ru.javaops.restaurantvoting.util.Views.Public;

@MappedSuperclass
@Getter
@Setter
public abstract class NamedEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    @JsonView(Public.class)
    protected String name;
}
