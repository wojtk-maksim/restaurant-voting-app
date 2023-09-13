package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Role;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.to.user.NewUserTo;
import ru.javaops.restaurantvoting.to.user.UpdatedUserTo;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

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
    public static final Set<Role> userRoles = EnumSet.of(Role.USER);
    public static final Set<Role> adminRoles = EnumSet.of(Role.USER, Role.ADMIN);
    public static final Set<Role> superAdminRoles = EnumSet.of(Role.USER, Role.ADMIN, Role.SUPER_ADMIN);
    public static final User admin = new User(ADMIN_ID, "admin", ADMIN_EMAIL, "admin", adminRoles);
    public static final User superAdmin = new User(SUPER_ADMIN_ID, "superAdmin", SUPER_ADMIN_EMAIL, "superAdmin", superAdminRoles);
    public static final User banned = new User(BANNED_ID, "banned", "banned@gmail.com", "banned", userRoles, false, false);
    public static final User deleted = new User(DELETED_ID, "deleted", "deleted@gmail.com", "deleted", userRoles, true, true);
    public static final User newUser = new User("newUser", "new.email@yandex.ru", "newPassword");
    public static final NewUserTo newUserTo = new NewUserTo(newUser.getName(), newUser.getEmail(), newUser.getPassword());
    public static final User updatedUser = new User(USER_ID, "updatedUser", "updated.email@yandex.ru", "updatedPassword", userRoles);
    private static final String USER_PASSWORD = "{noop}user";
    public static final User user = new User(USER_ID, "user", USER_EMAIL, USER_PASSWORD, userRoles);
    public static final UpdatedUserTo updatedUserTo = new UpdatedUserTo(updatedUser.getName(), updatedUser.getEmail(), user.getPassword(), updatedUser.getPassword());

    public static final List<User> users = List.of(admin, banned, deleted, superAdmin, user);

}
