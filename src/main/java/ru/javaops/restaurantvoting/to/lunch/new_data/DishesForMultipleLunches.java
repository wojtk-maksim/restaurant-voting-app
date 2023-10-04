package ru.javaops.restaurantvoting.to.lunch.new_data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
// https://stackoverflow.com/a/70630616/22653131
// Also keeps Map of (restaurant ID -> dishes' IDs), List of all restaurants' IDs and List of all dishes' IDs.
public class DishesForMultipleLunches {

    private List<RestaurantDishesPair> lunches;

    @JsonIgnore
    private LinkedHashMap<Long, List<Long>> dishIdsByRestaurantIds;

    @JsonIgnore
    private List<Long> allRestaurantIds;

    @JsonIgnore
    private List<Long> allDishIds;

    // Allows accessing lunches by index. Workaround for rejecting nested elements in validator.
    public RestaurantDishesPair getRestaurantDishesPair(int index) {
        return lunches.get(index);
    }

    // Creates Map of (restaurant ID -> dishes' IDs), List of all restaurants' IDs and List of all dishes' IDs.
    // The idea is to make service independent of DTOs and accept simple basic data types.
    public void setLunches(List<RestaurantDishesPair> lunches) {
        this.lunches = lunches;
        dishIdsByRestaurantIds = new LinkedHashMap<>(lunches.size());
        allRestaurantIds = new ArrayList<>(lunches.size());
        allDishIds = new ArrayList<>();
        lunches.forEach(l -> {
            Long restaurantId = l.getRestaurant();
            List<Long> dishIds = l.getDishes();
            dishIdsByRestaurantIds.put(restaurantId, dishIds);
            allRestaurantIds.add(restaurantId);
            allDishIds.addAll(dishIds);
        });
    }

    @Override
    public String toString() {
        return dishIdsByRestaurantIds.toString();
    }

}
