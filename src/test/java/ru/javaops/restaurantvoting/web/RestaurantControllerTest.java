package ru.javaops.restaurantvoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;
import static ru.javaops.restaurantvoting.TestUtil.extractJson;
import static ru.javaops.restaurantvoting.UserTestData.ADMIN_EMAIL;
import static ru.javaops.restaurantvoting.UserTestData.USER_EMAIL;
import static ru.javaops.restaurantvoting.util.JsonUtil.*;
import static ru.javaops.restaurantvoting.web.RestaurantController.RESTAURANT_URL;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(USER_EMAIL)
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANT_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertIterableEquals(restaurants, readValues(extractJson(result), Restaurant[].class)));
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANT_URL + "/" + BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertEquals(burgerKing, readValue(extractJson(result), Restaurant.class)));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void addNew() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_URL)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(getNew())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> {
                    Restaurant added = readValue(extractJson(result), Restaurant.class);
                    Restaurant newRestaurant = getNew();
                    newRestaurant.setId(added.getId());
                    assertEquals(newRestaurant, added);
                });
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(RESTAURANT_URL + "/" + BURGER_KING_ID)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(getUpdated())))
                .andDo(print())
                .andExpect(status().isOk());
        Restaurant restaurant = restaurantRepository.findById(BURGER_KING_ID).orElseThrow(NotFoundException::new);
        assertEquals(getUpdated(), restaurant);
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(RESTAURANT_URL + "/" + BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(restaurantRepository.findById(BURGER_KING_ID).isPresent());
    }
}
