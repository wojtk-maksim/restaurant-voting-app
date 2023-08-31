package ru.javaops.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "dish",
        uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "name"}, name = "uk_restaurant_dish"))
@Getter
@Setter
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}
