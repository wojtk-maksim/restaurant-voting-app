package ru.javaops.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "dish",
        uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "name"}, name = "uk_restaurant_dish"))
@Getter
@Setter
@NoArgsConstructor
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonIgnore
    //@JsonIdentityReference
    private Restaurant restaurant;

    public Dish(Integer id, String name, Integer price, Restaurant restaurant) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
    }

    public Dish(String name, Integer price, Restaurant restaurant) {
        this(null, name, price, restaurant);
    }

    public Dish(int id) {
        this(id, null, null, null);
    }
}
