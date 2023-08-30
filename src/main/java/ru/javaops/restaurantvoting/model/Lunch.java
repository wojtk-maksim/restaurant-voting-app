package ru.javaops.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(
        name = "lunch",
        uniqueConstraints = @UniqueConstraint(columnNames = {"date", "restaurant_id"}, name = "uk_date_restaurant"))
@Data
@EqualsAndHashCode(callSuper = true)
public class Lunch extends BaseEntity {

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "lunch_item",
            joinColumns = @JoinColumn(name = "lunch_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"lunch_id", "dish_id"}, name = "uk_lunch_dish"))
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Dish> dishes;
}
