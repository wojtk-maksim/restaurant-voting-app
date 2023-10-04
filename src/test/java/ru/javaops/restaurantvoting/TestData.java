package ru.javaops.restaurantvoting;

import static ru.javaops.restaurantvoting.UserTestData.*;
import static ru.javaops.restaurantvoting.util.JwtUtil.generateToken;

public class TestData {

    public static final Long NOT_FOUND = Long.MAX_VALUE;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String USER_JWT = "Bearer " + generateToken(user);
    public static final String ADMIN_JWT = "Bearer " + generateToken(admin);
    public static final String SUPER_ADMIN_JWT = "Bearer " + generateToken(superAdmin);
    public static final String BANNED_USER_JWT = "Bearer " + generateToken(banned);
    public static final String DELETED_USER_JWT = "Bearer " + generateToken(deleted);

}
