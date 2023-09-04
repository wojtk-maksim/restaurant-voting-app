package ru.javaops.restaurantvoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.to.RestaurantDishTo;
import ru.javaops.restaurantvoting.to.SimpleDishTo;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestUtil.extractJson;
import static ru.javaops.restaurantvoting.web.user.DishController.DISH_URL;

public class DishControllerTest extends AbstractControllerTest {

    @Autowired
    DishRepository dishRepository;

    @Test
    void getAllFromRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(DISH_URL, BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertIterableEquals(burgerKingDishes, jsonUtil.readValues(extractJson(result), RestaurantDishTo[].class)));
    }

    @Test
    void getFromRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(DISH_URL + "/" + BURGER_ID, BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertEquals(burger, jsonUtil.readValue(extractJson(result), Dish.class)));
    }

    @Test
    void addNewToRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(DISH_URL, BURGER_KING_ID)
                        .contentType(APPLICATION_JSON)
                        .content(jsonUtil.writeValue(getNew())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> {
                    RestaurantDishTo created = jsonUtil.readValue(extractJson(result), RestaurantDishTo.class);
                    RestaurantDishTo newDish = new RestaurantDishTo(created.id(), getNew().name(), getNew().price());
                    assertEquals(newDish, created);
                });
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(DISH_URL + "/" + BURGER_ID, BURGER_KING_ID)
                        .contentType(APPLICATION_JSON)
                        .content(jsonUtil.writeValue(getUpdated())))
                .andDo(print())
                .andExpect(status().isOk());
        RestaurantDishTo updated = dishRepository.getRestaurantDishTo(BURGER_KING_ID, BURGER_ID).orElseThrow(NotFoundException::new);
        assertEquals(getUpdated(), new SimpleDishTo(updated.name(), updated.price()));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(DISH_URL + "/" + BURGER_ID, BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(dishRepository.getRestaurantDishTo(BURGER_KING_ID, BURGER_KING_ID).isPresent());
    }
}
