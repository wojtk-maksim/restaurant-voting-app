package ru.javaops.restaurantvoting.web.public_access;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.TestData;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.RestaurantTestData.DELETED_RESTAURANT;
import static ru.javaops.restaurantvoting.TestData.NOT_FOUND;
import static ru.javaops.restaurantvoting.TestData.USER_JWT;
import static ru.javaops.restaurantvoting.web.public_access.DishController.DISHES_URL;

public class DishControllerTest extends AbstractControllerTest {

    @Autowired
    DishRepository dishRepository;

    @Test
    void getAllFromRestaurant() throws Exception {
        perform(MockMvcRequestBuilders
                .get(DISHES_URL, BURGER_KING_ID)
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> DISH_TO_MATCHER.matches(result, burgerKingDishesExcludeDeleted));
    }

    @Test
    void getAllFromDeletedRestaurant() throws Exception {
        perform(MockMvcRequestBuilders
                .get(DISHES_URL, DELETED_RESTAURANT.getId())
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isGone());
    }

    @Test
    void getFromRestaurant() throws Exception {
        perform(MockMvcRequestBuilders
                .get(DISHES_URL + "/" + BURGER_ID, BURGER_KING_ID)
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> DISH_TO_MATCHER.matches(result, BURGER));
    }

    @Test
    void getFromRestaurantNotFound() throws Exception {
        perform(MockMvcRequestBuilders
                .get(DISHES_URL + "/" + NOT_FOUND, BURGER_KING_ID)
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getFromRestaurantDeleted() throws Exception {
        perform(MockMvcRequestBuilders
                .get(DISHES_URL + "/" + DELETED_DISH.getId(), BURGER_KING_ID)
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isGone());
    }

}
