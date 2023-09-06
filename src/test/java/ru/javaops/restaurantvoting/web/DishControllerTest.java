package ru.javaops.restaurantvoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.to.RestaurantDishTo;
import ru.javaops.restaurantvoting.to.SimpleDishTo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestUtil.*;
import static ru.javaops.restaurantvoting.UserTestData.ADMIN_EMAIL;
import static ru.javaops.restaurantvoting.UserTestData.USER_EMAIL;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.DishController.DISH_URL;

public class DishControllerTest extends AbstractControllerTest {

    @Autowired
    DishRepository dishRepository;

    @Test
    @WithUserDetails(USER_EMAIL)
    void getAllFromRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(DISH_URL, BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> matches(parseObjects(result, RestaurantDishTo[].class), burgerKingDishes));
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void getFromRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(DISH_URL + "/" + BURGER_ID, BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> matches(parseObject(result, RestaurantDishTo.class), burgerRestaurantTo));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void addNewToRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(DISH_URL, BURGER_KING_ID)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(getNew())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> matches(parseObject(result, RestaurantDishTo.class), newlyAddedDish, "id"));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(DISH_URL + "/" + BURGER_ID, BURGER_KING_ID)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(updatedDish)))
                .andDo(print())
                .andExpect(status().isOk());
        RestaurantDishTo updated = dishRepository.getRestaurantDishTo(BURGER_KING_ID, BURGER_ID).orElseThrow(NotFoundException::new);
        assertEquals(getUpdated(), new SimpleDishTo(updated.name(), updated.price()));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(DISH_URL + "/" + BURGER_ID, BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(dishRepository.getRestaurantDishTo(BURGER_KING_ID, BURGER_KING_ID).isPresent());
    }
}
