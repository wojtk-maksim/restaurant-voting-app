package ru.javaops.restaurantvoting.web.admin;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.web.user.AbstractUserControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.UserTestData.*;
import static ru.javaops.restaurantvoting.web.admin.AdminUserController.ADMIN_USERS_URL;

@WithUserDetails(ADMIN_EMAIL)
public class AdminUserControllerTest extends AbstractUserControllerTest {

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_USERS_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> USER_MATCHER.matches(result, users, User[].class));
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_USERS_URL + "/" + USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> USER_MATCHER.matches(result, user, User.class));
    }

    @Test
    void enable() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(ADMIN_USERS_URL + "/" + USER_ID)
                        .param("enabled", "false"))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(userService.get(USER_ID).isEnabled());
    }

}
