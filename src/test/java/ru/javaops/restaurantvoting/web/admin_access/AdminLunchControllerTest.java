package ru.javaops.restaurantvoting.web.admin_access;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.service.LunchService;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.RestaurantTestData.getBurgerKing;
import static ru.javaops.restaurantvoting.UserTestData.ADMIN_EMAIL;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.admin_access.AdminLunchController.ADMIN_LUNCHES_URL;

public class AdminLunchControllerTest extends AbstractControllerTest {

    @Autowired
    private LunchRepository lunchRepository;

    @Autowired
    private LunchService lunchService;

    /*@Test
    @WithUserDetails(ADMIN_EMAIL)
    void getAllOnDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_LUNCHES_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> LUNCH_MATCHER.matches(result, allLunches, Lunch[].class));
    }*/

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getFromRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_LUNCHES_URL + "/" + DATE, BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
        //.andExpect(result -> null);
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void add() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_LUNCHES_URL + "/" + NEW_DATE, BURGER_KING_ID)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(dishIdsForNewLunch)))
                .andDo(print())
                .andExpect(status().isOk());
        //matches(lunchService.getFromRestaurantOnDate(BURGER_KING_ID, NEW_DATE), savedLunch, "id", "restaurant", "dishes.restaurant");
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void update() throws Exception {
        lunchService.add(getBurgerKing(), NEW_DATE, dishIdsForNewLunch);
        mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_LUNCHES_URL + "/" + NEW_DATE, BURGER_KING_ID)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(dishIdsForUpdatedLunch)))
                .andDo(print())
                .andExpect(status().isOk());
        LUNCH_MATCHER.matches(lunchService.getFromRestaurantOnDate(getBurgerKing(), NEW_DATE), updatedLunch);
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_LUNCHES_URL + "/" + DATE, BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk());
        assertThrows(NotFoundException.class, () -> lunchService.getFromRestaurantOnDate(getBurgerKing(), DATE));
    }
}
