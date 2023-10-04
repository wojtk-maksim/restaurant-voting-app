package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Role;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.to.user.ProfileTo;
import ru.javaops.restaurantvoting.to.user.UpdatedProfileTo;

import java.util.List;

import static ru.javaops.restaurantvoting.model.Role.BANNED;
import static ru.javaops.restaurantvoting.model.Role.DELETED;
import static ru.javaops.restaurantvoting.service.UserService.DELETED_USER_NAME;

public class UserTestData {

    public static final Matcher<User> USER_MATCHER = new Matcher<>(User.class, "password", "registered");
    public static final Matcher<ProfileTo> PROFILE_TO_MATCHER = new Matcher<>(ProfileTo.class, "password", "registered");

    public static final Long USER_ID = 1L;
    public static final Long ADMIN_ID = 2L;
    public static final Long SUPER_ADMIN_ID = 3L;
    public static final Long BANNED_ID = 4L;
    public static final Long DELETED_ID = 5L;

    public static final String USER_EMAIL = "user@yandex.ru";
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String SUPER_ADMIN_EMAIL = "super.admin@gmail.com";

    public static final User user = new User(USER_ID, "user", USER_EMAIL, "userPassword", Role.USER);
    public static final ProfileTo USER = new ProfileTo(USER_ID, user.getName(), USER_EMAIL, user.getPassword(), null, Role.USER);
    public static final User admin = new User(ADMIN_ID, "admin", ADMIN_EMAIL, "adminPassword", Role.ADMIN);
    public static final User superAdmin = new User(SUPER_ADMIN_ID, "superAdmin", SUPER_ADMIN_EMAIL, "superAdminPassword", Role.SUPER_ADMIN);
    public static final User banned = new User(BANNED_ID, "banned", "banned@gmail.com", "banned", Role.USER, false, false);
    public static final ProfileTo BANNED_USER = new ProfileTo(BANNED_ID, banned.getName(), banned.getEmail(), banned.getPassword(), null, BANNED);
    public static final User deleted = new User(DELETED_ID, "deleted", "deleted@gmail.com", "deleted", Role.USER, true, true);
    public static final ProfileTo DELETED_USER = new ProfileTo(deleted.getId(), DELETED_USER_NAME, deleted.getEmail(), deleted.getPassword(), null, DELETED);
    public static final User newUser = new User("newUser", "new.email@yandex.ru", "newPassword");
    public static final ProfileTo NEW_USER = new ProfileTo(null, newUser.getName(), newUser.getEmail(), newUser.getPassword(), null, Role.USER);
    public static final User updatedUser = new User(USER_ID, "updatedUser", "updated.email@yandex.ru", "updatedPassword", Role.USER);
    public static final UpdatedProfileTo UPDATED_PROFILE = new UpdatedProfileTo(updatedUser.getName(), updatedUser.getEmail(), user.getPassword(), updatedUser.getPassword());
    public static final ProfileTo UPDATED_USER = new ProfileTo(USER_ID, UPDATED_PROFILE.getName(), UPDATED_PROFILE.getEmail(), "updatedPassword", null, Role.USER);
    public static final UpdatedProfileTo invalidPasswordConfirmationUserTo = new UpdatedProfileTo("updatedUser", "updated.user@gmail.com", "wrongPasswordConfirmation", updatedUser.getPassword());

    public static final List<User> users = List.of(admin, banned, deleted, superAdmin, user);

}
