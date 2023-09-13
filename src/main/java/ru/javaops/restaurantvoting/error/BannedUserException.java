package ru.javaops.restaurantvoting.error;

import static ru.javaops.restaurantvoting.util.UserUtil.USER;

public class BannedUserException extends RuntimeException {

    public BannedUserException(Long id) {
        super(USER + " " + id + " is banned");
    }

}
