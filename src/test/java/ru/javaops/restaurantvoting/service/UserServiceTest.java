package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.error.authentication.BannedAuthenticationException;
import ru.javaops.restaurantvoting.error.authentication.DeletedAuthenticationException;
import ru.javaops.restaurantvoting.error.authentication.NotFoundAuthenticationException;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.to.user.AuthTo;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javaops.restaurantvoting.TestData.NOT_FOUND;
import static ru.javaops.restaurantvoting.UserTestData.*;

public class UserServiceTest extends AbstractTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void getForAuthentication() {
        AuthTo authUser = new AuthTo();
        authUser.setEmail(USER_EMAIL);
        authUser.setPassword(user.getPassword());
        USER_MATCHER.matches(userService.getForAuthentication(authUser), user);
    }

    @Test
    void getForAuthenticationPasswordIncorrect() {
        AuthTo authUser = new AuthTo();
        authUser.setEmail(USER_EMAIL);
        authUser.setPassword("1234qwer");
        assertThrows(IllegalArgumentException.class, () -> userService.getForAuthentication(authUser));
    }

    @Test
    void getForAuthenticationNotFound() {
        AuthTo authUser = new AuthTo();
        authUser.setEmail("not.found@gmail.com");
        authUser.setPassword(user.getPassword());
        assertThrows(NotFoundAuthenticationException.class, () -> userService.getForAuthentication(authUser));
    }

    @Test
    void getForAuthenticationBanned() {
        AuthTo authUser = new AuthTo();
        authUser.setEmail(BANNED_USER.getEmail());
        authUser.setPassword(BANNED_USER.getPassword());
        assertThrows(BannedAuthenticationException.class, () -> userService.getForAuthentication(authUser));
    }

    @Test
    void getForAuthenticationDeleted() {
        AuthTo authUser = new AuthTo();
        authUser.setEmail(DELETED_USER.getEmail());
        authUser.setPassword(DELETED_USER.getPassword());
        assertThrows(DeletedAuthenticationException.class, () -> userService.getForAuthentication(authUser));
    }

    @Test
    void get() {
        PROFILE_TO_MATCHER.matches(userService.get(USER_ID), USER);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> userService.get(NOT_FOUND));
    }

    @Test
    void create() {
        PROFILE_TO_MATCHER.matches(userService.create(NEW_USER.getName(), NEW_USER.getEmail(), NEW_USER.getPassword()), NEW_USER, "id");
    }

    @Test
    void update() {
        userService.update(USER_ID, UPDATED_PROFILE.getName(), UPDATED_PROFILE.getEmail(), UPDATED_PROFILE.getNewPassword());
        USER_MATCHER.matches(userRepository.get(USER_ID), updatedUser);
    }

    @Test
    void enable() {
        userService.enable(USER_ID, false);
        assertFalse(userRepository.get(USER_ID).isEnabled());
    }

    @Test
    void softDelete() {
        userService.softDelete(USER_ID);
        assertTrue(userRepository.get(USER_ID).isDeleted());
    }

    @Test
    void hardDelete() {
        userService.hardDelete(USER_ID);
        assertNull(userRepository.get(USER_ID));
    }

    @Test
    void getAllBanned() {
        assertEquals(userService.getAllBanned(), Set.of(banned.getId()));
    }

    @Test
    void getAllDeleted() {
        assertEquals(userService.getAllDeleted(), Set.of(deleted.getId()));
    }

}
