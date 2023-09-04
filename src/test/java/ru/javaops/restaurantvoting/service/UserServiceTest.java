package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaops.restaurantvoting.UserTestData.*;

public class UserServiceTest extends AbstractTest {

    @Autowired
    UserService userService;

    @Test
    void getAll() {
        assertEquals(List.of(admin, user), userService.getAll());
    }

    @Test
    void get() {
        assertEquals(user, userService.get(USER_ID));
    }

    @Test
    void getByEmail() {
        assertEquals(user, userService.getByEmail(USER_EMAIL));
    }

    @Test
    void create() {
        User created = userService.createOrUpdate(getNew());
        User newUser = getNew();
        newUser.setId(created.getId());
        assertEquals(newUser, created);
    }

    @Test
    void update() {
        User updated = userService.createOrUpdate(getUpdated());
        assertEquals(getUpdated(), updated);
    }

    @Test
    void delete() {
        userService.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> userService.delete(USER_ID));
    }
}
