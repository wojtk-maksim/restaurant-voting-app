package ru.javaops.restaurantvoting.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class NamedEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    protected String name;
}
