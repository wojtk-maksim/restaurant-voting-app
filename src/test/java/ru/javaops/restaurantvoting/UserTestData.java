package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Role;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.to.user.NewUserTo;
import ru.javaops.restaurantvoting.to.user.UpdatedUserTo;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static ru.javaops.restaurantvoting.web.user.UserController.DELETED;

public class UserTestData {

    public static final long USER_ID = 1;
    public static final long ADMIN_ID = 2;
    public static final long DELETED_ID = 3;

    public static final String USER_EMAIL = "user@yandex.ru";
    public static final String ADMIN_EMAIL = "admin@gmail.com";

    private static final String USER_PASSWORD = "{noop}user";

    public static final Set<Role> userRoles = EnumSet.of(Role.USER);
    public static final Set<Role> adminRoles = EnumSet.of(Role.USER, Role.ADMIN);

    public static final User user = new ru.javaops.restaurantvoting.model.User(USER_ID, "user", USER_EMAIL, USER_PASSWORD, userRoles);
    public static final User admin = new ru.javaops.restaurantvoting.model.User(ADMIN_ID, "admin", ADMIN_EMAIL, "admin", adminRoles);
    public static final User newUser = new User("newUser", "new.email@yandex.ru", "newPassword");
    public static final NewUserTo newUserTo = new NewUserTo(newUser.getName(), newUser.getEmail(), newUser.getPassword());
    public static final User updatedUser = new User(USER_ID, "updatedUser", "updated.email@yandex.ru", "updatedPassword", userRoles);
    public static final UpdatedUserTo updatedUserTo = new UpdatedUserTo(updatedUser.getName(), updatedUser.getEmail(), user.getPassword(), updatedUser.getPassword());
    public static final User deleted = new User(DELETED_ID, DELETED, null, null, null);

    public static final List<ru.javaops.restaurantvoting.model.User> users = List.of(admin, user);

    public static ru.javaops.restaurantvoting.model.User getNewUser() {
        return new ru.javaops.restaurantvoting.model.User(null, "newUser", "new.user@yandex.ru", "newUser", userRoles);
    }

    public static ru.javaops.restaurantvoting.model.User getUpdatedUser() {
        return new ru.javaops.restaurantvoting.model.User(USER_ID, "updatedUser", "updated.user@yandex.ru", "updatedUser", userRoles);
    }

    public static NewUserTo getNewUserTo() {
        ru.javaops.restaurantvoting.model.User newUser = getNewUser();
        return new NewUserTo(newUser.getName(), newUser.getEmail(), newUser.getPassword());
    }

    public static final User registeredUser = new User();

    public static UpdatedUserTo getUpdatedUserTo() {
        ru.javaops.restaurantvoting.model.User updatedUser = getUpdatedUser();
        return new UpdatedUserTo(updatedUser.getName(), updatedUser.getEmail(), USER_PASSWORD, updatedUser.getPassword());
    }

}
