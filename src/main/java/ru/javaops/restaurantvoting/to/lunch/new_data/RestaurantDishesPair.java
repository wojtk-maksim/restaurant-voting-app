package ru.javaops.restaurantvoting.to.lunch.new_data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
/*
 * https://stackoverflow.com/a/70630616/22653131
 *
 * Links DishesForMultipleLunches with DishesForSingleLunch.
 * Workaround for rejecting nested elements in validator.
 */
public class RestaurantDishesPair implements HasDishIds {

    private Long restaurant;

    private List<Long> dishes;

    // Allows accessing dishes by index. Workaround for rejecting nested elements in validator.
    public Long getDishId(int index) {
        return dishes.get(index);
    }

    @Override
    public String toString() {
        return "[restaurant = " + restaurant + ", dishes = " + dishes + "]";
    }

}
