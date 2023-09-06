package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Role;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.to.user.new_data.NewUserTo;
import ru.javaops.restaurantvoting.to.user.new_data.UpdatedUserTo;
import ru.javaops.restaurantvoting.to.user.registered.UserProfileTo;

import java.util.*;

import static ru.javaops.restaurantvoting.to.ToConverter.DELETED;

public class UserTestData {

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;

    public static final String USER_EMAIL = "user@yandex.ru";
    public static final String ADMIN_EMAIL = "admin@gmail.com";

    private static final String USER_PASSWORD = "{noop}user";

    public static final Set<Role> userRoles = EnumSet.of(Role.USER);
    public static final Set<Role> adminRoles = EnumSet.of(Role.USER, Role.ADMIN);

    public static final User user = new User(USER_ID, "user", USER_EMAIL, USER_PASSWORD, userRoles);
    public static final User admin = new User(ADMIN_ID, "admin", ADMIN_EMAIL, "admin", adminRoles);

    public static final List<User> users = List.of(admin, user);

    public static User getNewUser() {
        return new User(null, "newUser", "new.user@yandex.ru", "newUser", userRoles);
    }

    public static User getUpdatedUser() {
        return new User(USER_ID, "updatedUser", "updated.user@yandex.ru", "updatedUser", userRoles);
    }

    public static NewUserTo getNewUserTo() {
        User newUser = getNewUser();
        return new NewUserTo(newUser.getName(), newUser.getEmail(), newUser.getPassword());
    }

    public static UpdatedUserTo getUpdatedUserTo() {
        User updatedUser = getUpdatedUser();
        return new UpdatedUserTo(updatedUser.getName(), updatedUser.getEmail(), USER_PASSWORD, updatedUser.getPassword());
    }

    public static UserProfileTo getUserTo() {
        return new UserProfileTo(USER_ID, user.getName(), USER_EMAIL, user.getRegistered(), userRoles, user.isEnabled());
    }

    public static UserProfileTo getNewUserProfileTo() {
        User newUser = getNewUser();
        return new UserProfileTo(null, newUser.getName(), newUser.getEmail(), new Date(), newUser.getRoles(), newUser.isEnabled());
    }

    public static UserProfileTo getUpdatedUserProfileTo() {
        User updatedUser = getUpdatedUser();
        return new UserProfileTo(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail(), updatedUser.getRegistered(), updatedUser.getRoles(), updatedUser.isEnabled());
    }

    public static UserProfileTo getDeletedUserProfileTo() {
        return new UserProfileTo(USER_ID, DELETED, DELETED, user.getRegistered(), Collections.emptySet(), false);
    }
}
