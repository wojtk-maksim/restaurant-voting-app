package ru.javaops.restaurantvoting.web;

public class UrlData {

    public static final String API_PATH = "/api";
    public static final String ADMIN_PATH = "/admin";
    public static final String USERS_PATH = "/users";
    public static final String PROFILE_PATH = "/profile";
    public static final String RESTAURANTS_PATH = "/restaurants";
    public static final String DISHES_PATH = "/dishes";
    public static final String FULL_DISHES_PATH = RESTAURANTS_PATH + "/{restaurantId}" + DISHES_PATH;
    public static final String LUNCHES_PATH = "/lunches";
    public static final String FULL_LUNCHES_PATH = RESTAURANTS_PATH + "/{restaurantId}" + LUNCHES_PATH;
    public static final String PROBLEMS_PATH = "/problems";

}
