package ru.javaops.restaurantvoting.web.public_access;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.UserTestData.USER_EMAIL;
import static ru.javaops.restaurantvoting.web.public_access.DishController.DISHES_URL;

public class DishControllerTest extends AbstractControllerTest {

    @Autowired
    DishRepository dishRepository;

    @Test
    @WithUserDetails(USER_EMAIL)
    void getAllFromRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(DISHES_URL, BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> DISH_MATCHER.matches(result, burgerKingDishesExceptDeleted, Dish[].class));
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void getFromRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(DISHES_URL + "/" + BURGER_ID, BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> DISH_MATCHER.matches(result, burger, Dish.class));
    }

}
