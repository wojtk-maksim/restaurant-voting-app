package ru.javaops.restaurantvoting.web.public_access;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.TestData;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.TestData.NOT_FOUND;
import static ru.javaops.restaurantvoting.TestData.USER_JWT;
import static ru.javaops.restaurantvoting.UserTestData.*;
import static ru.javaops.restaurantvoting.web.public_access.UserController.USERS_URL;

public class UserControllerTest extends AbstractControllerTest {

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(USERS_URL + "/" + USER_ID)
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> PROFILE_TO_MATCHER.matches(result, USER, "email"));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(USERS_URL + "/" + NOT_FOUND)
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getDeleted() throws Exception {
        perform(MockMvcRequestBuilders
                .get(USERS_URL + "/" + DELETED_ID)
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> PROFILE_TO_MATCHER.matches(result, DELETED_USER, "email"));
    }

    @Test
    void getBanned() throws Exception {
        perform(MockMvcRequestBuilders
                .get(USERS_URL + "/" + BANNED_ID)
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> PROFILE_TO_MATCHER.matches(result, BANNED_USER, "email"));
    }

}
