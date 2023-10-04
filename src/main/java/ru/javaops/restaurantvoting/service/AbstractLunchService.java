package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.to.DishTo;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public abstract class AbstractLunchService {

    protected final CacheHelper cacheHelper;

    protected final LunchRepository lunchRepository;

    protected final DishRepository dishRepository;

    protected List<DishTo> getDishesInLunch(Long restaurantId, List<Long> dishIds) {
        Map<Long, DishTo> allDishesInRestaurant = cacheHelper.getAllDishesCachedFromRestaurant(restaurantId);
        return dishIds.stream()
                .map(allDishesInRestaurant::get)
                .toList();
    }

}
