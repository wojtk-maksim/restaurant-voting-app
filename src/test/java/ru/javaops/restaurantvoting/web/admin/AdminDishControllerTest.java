package ru.javaops.restaurantvoting.web.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.service.DishService;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestUtil.matches;
import static ru.javaops.restaurantvoting.TestUtil.parseObject;
import static ru.javaops.restaurantvoting.UserTestData.ADMIN_EMAIL;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.admin.AdminDishController.ADMIN_DISHES_URL;

public class AdminDishControllerTest extends AbstractControllerTest {

    @Autowired
    private DishService dishService;

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void addNewToRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_DISHES_URL, BURGER_KING_ID)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> matches(parseObject(result, Dish.class), savedDish, "id", "restaurant"));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_DISHES_URL + "/" + BURGER_ID, BURGER_KING_ID)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(updatedDish)))
                .andDo(print())
                .andExpect(status().isOk());
        matches(dishService.getFromRestaurant(BURGER_KING_ID, BURGER_ID), updatedDish, "restaurant");
    }

    /*@Test
    @WithUserDetails(ADMIN_EMAIL)
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_DISHES_URL + "/" + BURGER_ID, BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk());

    }*/

}
