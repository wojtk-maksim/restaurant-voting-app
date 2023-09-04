package ru.javaops.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(
        name = "lunch",
        uniqueConstraints = @UniqueConstraint(columnNames = {"date", "restaurant_id"}, name = "uk_date_restaurant"))
@Getter
@Setter
@NoArgsConstructor
public class Lunch extends BaseEntity {

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "restaurant_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_restaurant_lunch",
                    foreignKeyDefinition = "FOREIGN KEY(restaurant_id) REFERENCES Restaurant(id) ON DELETE CASCADE"
            ))
    private Restaurant restaurant;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "lunch_item",
            joinColumns = @JoinColumn(name = "lunch_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id"),
            inverseForeignKey = @ForeignKey(
                    name = "fk_dish_lunch",
                    foreignKeyDefinition = "FOREIGN KEY(dish_id) REFERENCES Dish(id) ON DELETE CASCADE"
            ),
            uniqueConstraints = @UniqueConstraint(columnNames = {"lunch_id", "dish_id"}, name = "uk_lunch_dish"))
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Dish> dishes;

    public Lunch(LocalDate date, Restaurant restaurant, Set<Dish> dishes) {
        this.date = date;
        this.restaurant = restaurant;
        this.dishes = dishes;
    }
}
