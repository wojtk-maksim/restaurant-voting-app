package ru.javaops.restaurantvoting.util;

import ru.javaops.restaurantvoting.NamedEnablableDeletable;
import ru.javaops.restaurantvoting.error.DeletedEntityException;
import ru.javaops.restaurantvoting.error.NotAvailableException;
import ru.javaops.restaurantvoting.service.CacheHelper;
import ru.javaops.restaurantvoting.to.RestaurantTo;

public class RestaurantUtil extends ValidationUtil {

    public static final String RESTAURANT_CODE = "restaurant";

    private static CacheHelper cacheHelper;

    public static void setCacheHelper(CacheHelper cacheHelper) {
        RestaurantUtil.cacheHelper = cacheHelper;
    }

    public static RestaurantTo getRestaurantIfExists(Long id) {
        RestaurantTo restaurant = cacheHelper.getRestaurantsCached().get(id);
        checkRestaurantFound(restaurant, id);
        return restaurant;
    }

    public static void checkRestaurantFound(Object restaurant, Object identifier) {
        checkFound(RESTAURANT_CODE, restaurant, identifier);
    }

    public static void checkRestaurantAvailable(NamedEnablableDeletable restaurant) {
        if (!restaurant.isEnabled()) {
            throw new NotAvailableException(messageSourceAccessor.getMessage(
                    "error.notAvailable." + RESTAURANT_CODE,
                    new String[]{restaurant.getName()}
            ));
        }
    }

    public static void checkRestaurantDeleted(NamedEnablableDeletable restaurant) {
        if (restaurant.isDeleted()) {
            throw new DeletedEntityException(messageSourceAccessor.getMessage(
                    "error.deleted." + RESTAURANT_CODE,
                    new String[]{restaurant.getName()}
            ));
        }
    }

}
