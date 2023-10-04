package ru.javaops.restaurantvoting.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import ru.javaops.restaurantvoting.NamedEnablableDeletable;

@MappedSuperclass
@Getter
@Setter
public abstract class NamedEnablableDeletableEntity extends EnablableEntity implements NamedEnablableDeletable {

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "deleted", nullable = false, columnDefinition = "bool default false")
    protected boolean deleted;

    @Override
    public String toString() {
        return "[id = " + id + ", name = '" + name + "']";
    }

}
