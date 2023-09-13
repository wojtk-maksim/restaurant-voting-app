package ru.javaops.restaurantvoting.web.admin;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.service.RestaurantService;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;
import static ru.javaops.restaurantvoting.UserTestData.ADMIN_EMAIL;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.admin.AdminRestaurantController.ADMIN_RESTAURANTS_URL;

public class AdminRestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private EntityManager em;

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_RESTAURANTS_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("enabled")))
                .andExpect(result -> RESTAURANT_MATCHER.matches(result, restaurants, Restaurant[].class));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_RESTAURANTS_URL + "/" + BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> RESTAURANT_MATCHER.matches(result, burgerKing, Restaurant.class));
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
                .andExpect(result -> RESTAURANT_MATCHER.matches(result, savedRestaurant, Restaurant.class, "id"));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_RESTAURANTS_URL + "/" + BURGER_KING_ID)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(status().isOk());
        RESTAURANT_MATCHER.matches(restaurantService.get(BURGER_KING_ID), updatedRestaurant);
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_RESTAURANTS_URL + "/" + BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk());
        assertTrue(restaurantService.get(BURGER_KING_ID).isDeleted());
    }

}
