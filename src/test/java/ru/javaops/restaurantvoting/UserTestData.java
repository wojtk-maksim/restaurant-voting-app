package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Role;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.to.user.NewAccount;
import ru.javaops.restaurantvoting.to.user.UpdatedAccount;

import java.util.List;

public class UserTestData {

    public static final TestUtil.Matcher<User> USER_MATCHER = new TestUtil.Matcher<>(List.of("password", "registered"));

    public static final Long USER_ID = 1L;
    public static final Long ADMIN_ID = 2L;
    public static final Long SUPER_ADMIN_ID = 3L;
    public static final Long BANNED_ID = 4L;
    public static final Long DELETED_ID = 5L;

    public static final String USER_EMAIL = "user@yandex.ru";
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String SUPER_ADMIN_EMAIL = "super.admin@gmail.com";
    public static final String BANNED_USER_EMAIL = "deleted@gmail.com";


    public static final User user = new User(USER_ID, "user", USER_EMAIL, "userPassword", Role.USER);

    public static User getUser() {
        return new User(USER_ID, "user", USER_EMAIL, "userPassword", Role.USER);
    }

    public static final User admin = new User(ADMIN_ID, "admin", ADMIN_EMAIL, "adminPassword", Role.ADMIN);
    public static final User superAdmin = new User(SUPER_ADMIN_ID, "superAdmin", SUPER_ADMIN_EMAIL, "superAdminPassword", Role.SUPER_ADMIN);
    public static final User banned = new User(BANNED_ID, "banned", "banned@gmail.com", "banned", Role.USER, false, false);
    public static final User deleted = new User(DELETED_ID, "deleted", "deleted@gmail.com", "deleted", Role.USER, true, true);

    public static final User newUser = new User("newUser", "new.email@yandex.ru", "newPassword");
    public static final NewAccount NEW_PROFILE = new NewAccount(newUser.getName(), newUser.getEmail(), newUser.getPassword());
    public static final User updatedUser = new User(USER_ID, "updatedUser", "updated.email@yandex.ru", "updatedPassword", Role.USER);
    public static final UpdatedAccount UPDATED_PROFILE = new UpdatedAccount(updatedUser.getName(), updatedUser.getEmail(), user.getPassword(), updatedUser.getPassword());
    public static final UpdatedAccount invalidPasswordConfirmationUserTo = new UpdatedAccount("updatedUser", "updated.user@gmail.com", "wrongPassword", "newPassword");

    public static final List<User> users = List.of(admin, banned, deleted, superAdmin, user);

}
