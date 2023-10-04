package ru.javaops.restaurantvoting.web.admin_access;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.TestData;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.web.AbstractUserControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.TestData.NOT_FOUND;
import static ru.javaops.restaurantvoting.UserTestData.*;
import static ru.javaops.restaurantvoting.model.Role.BANNED;
import static ru.javaops.restaurantvoting.model.Role.DELETED;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.admin_access.AdminUserController.ADMIN_USERS_URL;

public class AdminUserControllerTest extends AbstractUserControllerTest {

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_USERS_URL)
                .header(TestData.AUTHORIZATION_HEADER, TestData.ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> USER_MATCHER.matches(result, users));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_USERS_URL + "/" + USER_ID)
                .header(TestData.AUTHORIZATION_HEADER, TestData.ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> USER_MATCHER.matches(result, user));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_USERS_URL + "/" + NOT_FOUND)
                .header(TestData.AUTHORIZATION_HEADER, TestData.ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        perform(MockMvcRequestBuilders.post(ADMIN_USERS_URL)
                .header(TestData.AUTHORIZATION_HEADER, TestData.ADMIN_JWT)
                .contentType(APPLICATION_JSON)
                .content(writeValue(NEW_USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> USER_MATCHER.matches(result, newUser, "id"));
    }

    @Test
    void createNameEmailExists() throws Exception {
        perform(MockMvcRequestBuilders.post(ADMIN_USERS_URL)
                .header(TestData.AUTHORIZATION_HEADER, TestData.ADMIN_JWT)
                .contentType(APPLICATION_JSON)
                .content(writeValue(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(ADMIN_USERS_URL + "/" + USER_ID)
                .header(TestData.AUTHORIZATION_HEADER, TestData.ADMIN_JWT)
                .contentType(APPLICATION_JSON)
                .content(writeValue(UPDATED_USER)))
                .andDo(print())
                .andExpect(status().isOk());
        PROFILE_TO_MATCHER.matches(userService.get(USER_ID), UPDATED_USER);
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(ADMIN_USERS_URL + "/" + USER_ID)
                .header(TestData.AUTHORIZATION_HEADER, TestData.ADMIN_JWT)
                .param("enabled", "false"))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(BANNED, userService.get(USER_ID).getRole());
    }

    @Test
    void softDelete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_USERS_URL + "/" + USER_ID)
                .header(TestData.AUTHORIZATION_HEADER, TestData.ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(DELETED, userService.get(USER_ID).getRole());
    }

    @Test
    void hardDelete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_USERS_URL + "/" + USER_ID + "/hard-delete")
                .header(TestData.AUTHORIZATION_HEADER, TestData.SUPER_ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isOk());
        assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

}
