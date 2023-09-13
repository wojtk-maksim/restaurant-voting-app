package ru.javaops.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javaops.restaurantvoting.util.Views.Admin;
import ru.javaops.restaurantvoting.util.Views.Public;

@Entity
@Table(
        name = "dish",
        uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "name"}, name = "uk_restaurant_dish"
        ))
@Getter
@Setter
@NoArgsConstructor
public class Dish extends NamedEntity implements Enablable, Deletable {

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    @JsonView(Public.class)
    protected boolean enabled = true;
    @Column(name = "price", nullable = false)
    @JsonView(Public.class)
    private Integer price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;
    @Column(name = "deleted", nullable = false, columnDefinition = "bool default false")
    @JsonView(Admin.class)
    private boolean deleted;

    public Dish(Long id, String name, Integer price, Restaurant restaurant, boolean enabled, boolean deleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
        this.enabled = enabled;
        this.deleted = deleted;
    }

    public Dish(Long id, String name, Integer price, Restaurant restaurant) {
        this(id, name, price, restaurant, true, false);
    }

    public Dish(String name, Integer price, Restaurant restaurant) {
        this(null, name, price, restaurant);
    }

}
