package ru.javaops.restaurantvoting.util;

import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.to.user.UserTo;

public class ToUtil {

    public static final String DELETED = "DELETED";

    public static UserTo userTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getRegistered(), user.getRoles(), user.isEnabled(), user.isDeleted());
    }

    public static UserTo deletedUserTo(Long id) {
        return new UserTo(id, DELETED, null, null, null, false, true);
    }

}
