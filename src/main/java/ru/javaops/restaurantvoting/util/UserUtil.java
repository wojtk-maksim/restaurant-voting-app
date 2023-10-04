package ru.javaops.restaurantvoting.util;

import ru.javaops.restaurantvoting.HasIdAndEmail;
import ru.javaops.restaurantvoting.error.authentication.BannedAuthenticationException;
import ru.javaops.restaurantvoting.error.authentication.DeletedAuthenticationException;
import ru.javaops.restaurantvoting.error.authentication.NotFoundAuthenticationException;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.service.UserService;

public class UserUtil extends ValidationUtil {

    public static final String USER_CODE = "user";

    private static UserService userService;

    public static void setUserService(UserService userService) {
        UserUtil.userService = userService;
    }

    public static void checkUserFound(Object object, Object identifier) {
        checkFound(USER_CODE, object, identifier);
    }

    public static void checkUsernameFound(User user, Object identifier) {
        if (user == null) {
            throw new NotFoundAuthenticationException(messageSourceAccessor.getMessage(
                    "error.authNotFound", new String[]{identifier.toString()}
            ));
        }
    }

    public static void checkBannedOrDeleted(HasIdAndEmail user) {
        Long id = user.getId();
        if (userService.getAllDeleted().contains(id)) {
            throw new DeletedAuthenticationException(messageSourceAccessor.getMessage(
                    "error.authDeleted", new String[]{user.getEmail()}
            ));
        }
        if (userService.getAllBanned().contains(id)) {
            throw new BannedAuthenticationException(messageSourceAccessor.getMessage(
                    "error.authBanned", new String[]{user.getEmail()}
            ));
        }
    }

}
