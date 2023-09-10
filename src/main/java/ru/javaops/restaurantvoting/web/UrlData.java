package ru.javaops.restaurantvoting.web;

public class UrlData {

    public static final String API = "/api";

    public static final String ADMIN = "/admin";

    public static final String USERS = "/users";

    public static final String RESTAURANTS = "/restaurants";

    public static final String DISHES = RESTAURANTS + "/{restaurantId}/dishes";

    public static final String LUNCHES = RESTAURANTS + "/{restaurantId}/lunches";

    public static final String VOTING = "/voting";

}
