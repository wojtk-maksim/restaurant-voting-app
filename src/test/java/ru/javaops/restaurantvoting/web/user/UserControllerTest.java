package ru.javaops.restaurantvoting.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.TestUtil.matches;
import static ru.javaops.restaurantvoting.TestUtil.parseObject;
import static ru.javaops.restaurantvoting.UserTestData.*;
import static ru.javaops.restaurantvoting.web.user.UserController.USERS_URL;

public class UserControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(USER_EMAIL)
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USERS_URL + "/" + USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertFalse(result.getResponse().getContentAsString().contains("deleted")))
                .andExpect(result -> matches(parseObject(result, User.class), user, "registered", "password"));
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void getDeleted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USERS_URL + "/" + DELETED_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertFalse(result.getResponse().getContentAsString().contains("deleted")))
                .andExpect(result -> matches(parseObject(result, User.class), deleted, "registered", "password"));
    }
}
