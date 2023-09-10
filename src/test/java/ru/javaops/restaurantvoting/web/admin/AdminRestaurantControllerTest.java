package ru.javaops.restaurantvoting.web.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.service.RestaurantService;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;
import static ru.javaops.restaurantvoting.TestUtil.*;
import static ru.javaops.restaurantvoting.UserTestData.ADMIN_EMAIL;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.admin.AdminRestaurantController.ADMIN_RESTAURANTS_URL;

public class AdminRestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_RESTAURANTS_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("enabled")))
                .andExpect(result -> matches(parseObjects(result, Restaurant[].class), restaurants));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_RESTAURANTS_URL + "/" + BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> matches(parseObject(result, Restaurant.class), burgerKing));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void addNew() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_RESTAURANTS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> matches(parseObject(result, Restaurant.class), savedRestaurant, "id"));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_RESTAURANTS_URL + "/" + BURGER_KING_ID)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(status().isOk());
        matches(restaurantService.get(BURGER_KING_ID), updatedRestaurant, "dishes");
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_RESTAURANTS_URL + "/" + BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(restaurantRepository.findById(BURGER_KING_ID).isPresent());
    }

}
