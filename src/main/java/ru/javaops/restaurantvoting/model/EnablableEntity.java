package ru.javaops.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import ru.javaops.restaurantvoting.util.Views;

@MappedSuperclass
@Getter
@Setter
public abstract class EnablableEntity extends BaseEntity implements Enablable {

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    @JsonView(Views.Public.class)
    protected boolean enabled = true;

}
