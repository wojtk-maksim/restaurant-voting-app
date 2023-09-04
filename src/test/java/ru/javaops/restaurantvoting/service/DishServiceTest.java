package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.to.RestaurantDishTo;
import ru.javaops.restaurantvoting.to.SimpleDishTo;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.LunchTestData.dishIdsForNewLunch;
import static ru.javaops.restaurantvoting.LunchTestData.dishesForNewLunch;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;

public class DishServiceTest extends AbstractTest {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private DishService dishService;

    @Test
    void add() {
        RestaurantDishTo actual = dishService.add(newDish, BURGER_KING_ID);
        RestaurantDishTo expected = new RestaurantDishTo(actual.id(), newDish.name(), newDish.price());
        assertEquals(expected, actual);
    }

    @Test
    void update() {
        dishService.update(BURGER_KING_ID, BURGER_ID, updatedDish);
        RestaurantDishTo actual = dishRepository.getRestaurantDishTo(BURGER_KING_ID, BURGER_ID).orElseThrow(NotFoundException::new);
        assertEquals(updatedDish, new SimpleDishTo(actual.name(), actual.price()));
    }

    @Test
    void getByIds() {
        Set<Dish> actual = dishService.getByIds(BURGER_KING_ID, dishIdsForNewLunch);
        assertEquals(dishesForNewLunch, actual);
    }
}
