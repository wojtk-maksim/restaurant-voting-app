package ru.javaops.restaurantvoting.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;

@Entity
@Table(name = "restaurant")
public class Restaurant extends NamedEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("price")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Dish> dishes;
}
