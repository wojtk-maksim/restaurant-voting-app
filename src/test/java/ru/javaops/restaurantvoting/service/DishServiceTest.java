package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.repository.DishRepository;

import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestUtil.matches;

public class DishServiceTest extends AbstractTest {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private DishService dishService;

    @Test
    void add() {
        Dish actual = dishService.add(BURGER_KING_ID, newDish);
        matches(actual, savedDish, "id", "restaurant");
    }

    @Test
    void update() {
        dishService.update(BURGER_KING_ID, BURGER_ID, newDish);
        Dish actual = dishRepository.findById(BURGER_ID).orElseThrow(NotFoundException::new);
        matches(actual, updatedDish, "restaurant");
    }

}
