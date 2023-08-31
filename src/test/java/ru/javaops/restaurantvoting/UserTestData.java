package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.User;

import java.util.EnumSet;

import static ru.javaops.restaurantvoting.model.Role.ADMIN;
import static ru.javaops.restaurantvoting.model.Role.USER;

public class UserTestData {

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;

    public static final String USER_EMAIL = "user@yandex.ru";

    public static final User user = new User(USER_ID, "user", USER_EMAIL, "user", EnumSet.of(USER));
    public static final User admin = new User(ADMIN_ID, "admin", "admin@gmail.com", "admin", EnumSet.of(USER, ADMIN));
    private static final User newUser = new User(null, "newUser", "new.user@yandex.ru", "newUser", EnumSet.of(USER));
    private static final User updatedUser = new User(USER_ID, "updatedUser", "updated.user@yandex.ru", "updatedUser", EnumSet.of(USER));

    public static User getNew() {
        return newUser;
    }

    public static User getUpdated() {
        return updatedUser;
    }
}
