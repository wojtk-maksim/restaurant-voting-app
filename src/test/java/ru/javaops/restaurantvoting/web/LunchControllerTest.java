package ru.javaops.restaurantvoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.service.LunchService;
import ru.javaops.restaurantvoting.to.LunchTo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestUtil.extractJson;
import static ru.javaops.restaurantvoting.UserTestData.ADMIN_EMAIL;
import static ru.javaops.restaurantvoting.UserTestData.USER_EMAIL;
import static ru.javaops.restaurantvoting.util.JsonUtil.readValue;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.LunchController.LUNCH_URL;

public class LunchControllerTest extends AbstractControllerTest {

    @Autowired
    private LunchRepository lunchRepository;

    @Autowired
    private LunchService lunchService;

    @Test
    @WithUserDetails(USER_EMAIL)
    void getFromRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(LUNCH_URL, BURGER_KING_ID, DATE_AS_STRING))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertEquals(burgerKingLunch, readValue(extractJson(result), LunchTo.class)));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void add() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(LUNCH_URL, BURGER_KING_ID, NEW_DATE_AS_STRING)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(dishIdsForNewLunch)))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(newLunch, lunchService.get(BURGER_KING_ID, NEW_DATE));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(LUNCH_URL, BURGER_KING_ID, DATE)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(dishIdsForUpdatedLunch)))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(updatedLunch, lunchService.get(BURGER_KING_ID, DATE));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(LUNCH_URL, BURGER_KING_ID, DATE))
                .andDo(print())
                .andExpect(status().isOk());
        assertThrows(NotFoundException.class, () -> lunchService.get(BURGER_KING_ID, DATE));
    }
}
