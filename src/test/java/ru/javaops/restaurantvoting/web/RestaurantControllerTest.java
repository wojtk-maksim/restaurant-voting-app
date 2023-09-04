package ru.javaops.restaurantvoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import static ru.javaops.restaurantvoting.web.user.RestaurantController.RESTAURANT_URL;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANT_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertIterableEquals(restaurants, jsonUtil.readValues(extractJson(result), Restaurant[].class)));
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANT_URL + "/" + BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertEquals(burgerKing, jsonUtil.readValue(extractJson(result), Restaurant.class)));
    }

    @Test
    void addNew() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_URL)
                        .contentType(APPLICATION_JSON)
                        .content(jsonUtil.writeValue(getNew())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> {
                    Restaurant added = jsonUtil.readValue(extractJson(result), Restaurant.class);
                    Restaurant newRestaurant = getNew();
                    newRestaurant.setId(added.getId());
                    assertEquals(newRestaurant, added);
                });
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(RESTAURANT_URL + "/" + BURGER_KING_ID)
                        .contentType(APPLICATION_JSON)
                        .content(jsonUtil.writeValue(getUpdated())))
                .andDo(print())
                .andExpect(status().isOk());
        Restaurant restaurant = restaurantRepository.findById(BURGER_KING_ID).orElseThrow(NotFoundException::new);
        assertEquals(getUpdated(), restaurant);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(RESTAURANT_URL + "/" + BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(restaurantRepository.findById(BURGER_KING_ID).isPresent());
    }
}
