package ru.javaops.restaurantvoting.web.admin_access;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.service.LunchService;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestData.*;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.admin_access.AdminLunchController.ADMIN_LUNCHES_URL;

public class AdminLunchControllerTest extends AbstractControllerTest {

    @Autowired
    private LunchService lunchService;

    @Test
    void getFromRestaurant() throws Exception {
        perform(MockMvcRequestBuilders
                .get(ADMIN_LUNCHES_URL + "/" + DATE, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> LUNCH_TO_MATCHER.matches(result, BURGER_KING_LUNCH));
    }

    @Test
    void getFromRestaurantNotFound() throws Exception {
        perform(MockMvcRequestBuilders
                .get(ADMIN_LUNCHES_URL + "/" + NEW_DATE, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void add() throws Exception {
        perform(MockMvcRequestBuilders
                .post(ADMIN_LUNCHES_URL + "/" + NEW_DATE, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, ADMIN_JWT)
                .contentType(APPLICATION_JSON)
                .content(writeValue(DISHES_FOR_NEW_LUNCH)))
                .andDo(print())
                .andExpect(status().isOk());
        LUNCH_TO_MATCHER.matches(
                lunchService.getFromRestaurantOnDate(BURGER_KING_ID, NEW_DATE).getRestaurantItem(),
                NEW_LUNCH);
    }

    @Test
    void update() throws Exception {
        lunchService.createOrUpdate(BURGER_KING_ID, NEW_DATE, dishIdsForNewLunch);
        perform(MockMvcRequestBuilders
                .post(ADMIN_LUNCHES_URL + "/" + NEW_DATE, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, ADMIN_JWT)
                .contentType(APPLICATION_JSON)
                .content(writeValue(DISHES_FOR_UPDATED_LUNCH)))
                .andDo(print())
                .andExpect(status().isOk());
        LUNCH_TO_MATCHER.matches(
                lunchService.getFromRestaurantOnDate(BURGER_KING_ID, NEW_DATE).getRestaurantItem(),
                UPDATED_LUNCH);
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders
                .patch(ADMIN_LUNCHES_URL + "/" + DATE, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, ADMIN_JWT)
                .param("enabled", "false"))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(lunchService.getFromRestaurantOnDate(BURGER_KING_ID, DATE).getRestaurantItem().isEnabled());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(ADMIN_LUNCHES_URL + "/" + DATE + "/hard-delete", BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, SUPER_ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isOk());
        assertThrows(NotFoundException.class, () -> lunchService.getFromRestaurantOnDate(BURGER_KING_ID, DATE));
    }

}
