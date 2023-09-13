package ru.javaops.restaurantvoting.util;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.javaops.restaurantvoting.error.BannedUserException;
import ru.javaops.restaurantvoting.error.EmailOccupiedException;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.service.UserService;
import ru.javaops.restaurantvoting.to.user.UserTo;
import ru.javaops.restaurantvoting.web.AuthUser;

import static ru.javaops.restaurantvoting.config.SecurityConfig.PASSWORD_ENCODER;
import static ru.javaops.restaurantvoting.util.validation.ValidationUtil.checkDeleted;
import static ru.javaops.restaurantvoting.util.validation.ValidationUtil.checkExists;

public class UserUtil {

    public static final String USER = "User";

    private static UserService userService;

    public static void setUserService(UserService userService) {
        UserUtil.userService = userService;
    }

    public static void checkEmailFound(String email) {
        if (email != null) {
            throw new EmailOccupiedException(email);
        }
    }

    public static AuthUser getActiveUser() {
        return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static void checkUserExists(User user, Object id) {
        checkExists(user, USER, id);
    }

    public static void checkBanned(User user) {
        if (!user.isEnabled()) {
            throw new BannedUserException(user.getId());
        }
    }

    public static void checkUserDeleted(User user, Long id) {
        checkDeleted(user, USER, id);
    }

    public static void checkUserDeleted(UserTo user, Long id) {
        checkDeleted(user, USER, id);
    }

    public static String encodePassword(String password) {
        return PASSWORD_ENCODER.encode(password);
    }

}
