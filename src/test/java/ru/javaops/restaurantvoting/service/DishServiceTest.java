package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.repository.DishRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.getBurgerKing;
import static ru.javaops.restaurantvoting.TestUtil.NOT_FOUND;

public class DishServiceTest extends AbstractTest {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private DishService dishService;

    @Test
    void getAll() {
        DISH_MATCHER.matches(new ArrayList<>(dishService.getAllFromRestaurant(getBurgerKing()).values()), burgerKingDishes);
    }

    @Test
    void get() {
        DISH_MATCHER.matches(dishService.getFromRestaurant(getBurgerKing(), BURGER_ID), burger);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> dishService.getFromRestaurant(getBurgerKing(), NOT_FOUND));
    }

    @Test
    void add() {
        DISH_MATCHER.matches(dishService.add(getBurgerKing(), newDish), savedDish, "id");
    }

    @Test
    void update() {
        dishService.update(getBurgerKing(), getBurger(), newDish);
        DISH_MATCHER.matches(dishRepository.get(getBurgerKing(), BURGER_ID), updatedDish);
    }

    @Test
    void delete() {
        dishService.delete(getBurgerKing(), getBurger());
        assertTrue(dishRepository.get(getBurgerKing(), BURGER_ID).isDeleted());
    }

}
