package ru.javaops.restaurantvoting.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class NamedEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
}
