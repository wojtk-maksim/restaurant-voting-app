package ru.javaops.restaurantvoting.web.validation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javaops.restaurantvoting.service.CacheHelper;
import ru.javaops.restaurantvoting.to.DishTo;
import ru.javaops.restaurantvoting.to.RestaurantTo;
import ru.javaops.restaurantvoting.to.lunch.new_data.DishesForMultipleLunches;
import ru.javaops.restaurantvoting.to.lunch.new_data.DishesForSingleLunch;
import ru.javaops.restaurantvoting.to.lunch.new_data.HasDishIds;
import ru.javaops.restaurantvoting.to.lunch.new_data.RestaurantDishesPair;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static ru.javaops.restaurantvoting.util.DishUtil.DISH_CODE;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.getRestaurantIfExists;

@Component
@Slf4j
@AllArgsConstructor
public class DishesForLunchValidator implements Validator {

    private final CacheHelper cacheHelper;

    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return DishesForSingleLunch.class.isAssignableFrom(clazz) ||
                DishesForMultipleLunches.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        // Create or update single lunch. Need to validate a List of dishes' IDs.
        if (target instanceof DishesForSingleLunch lunch) {
            // Need to retrieve ID of restaurant. Split request URI by '/':
            // '' / 'api' / 'admin' / 'restaurants' / '{restaurantId}' / 'lunches' / '{date}'
            String uri = request.getRequestURI();
            Long restaurantId = Long.valueOf(uri.split("/")[4]);
            RestaurantTo restaurant = getRestaurantIfExists(restaurantId);
            log.debug("Validate dishes {} for lunch inr restaurant {}", lunch, restaurant);
            validateDishes(lunch, restaurantId, restaurant, errors);
        }

        // Batch create or update lunches. Need to validate a Map of (key -> restaurant ID : value -> dishes' IDs).
        else if (target instanceof DishesForMultipleLunches dishesForMultipleLunches) {
            List<RestaurantDishesPair> lunches = dishesForMultipleLunches.getLunches();
            log.debug("Validate batch input of lunches {}", lunches);
            for (int i = 0; i < lunches.size(); i++) {
                RestaurantDishesPair lunch = lunches.get(i);

                Long restaurantId = lunch.getRestaurant();
                RestaurantTo restaurant = getRestaurantIfExists(restaurantId);

                // https://stackoverflow.com/a/23734505/22653131
                errors.pushNestedPath("lunches[" + i + "]");
                validateDishes(lunch, restaurantId, restaurant, errors);
                errors.popNestedPath();
            }
        }
    }

    private void validateDishes(HasDishIds dishesForLunch, Long restaurantId, RestaurantTo restaurant, Errors errors) {
        int totalNumberOdDishes = dishesForLunch.getDishes().size();
        if (new HashSet<>(dishesForLunch.getDishes()).size() < totalNumberOdDishes) {
            errors.rejectValue("dishes", "error.duplicatedDishes");
        }

        Map<Long, DishTo> dishes = cacheHelper.getAllDishesCachedFromRestaurant(restaurantId);

        for (int i = 0; i < totalNumberOdDishes; i++) {
            Long id = dishesForLunch.getDishId(i);
            DishTo dish = dishes.get(id);
            if (dish == null) {
                errors.rejectValue(
                        "dishes[" + i + "]", "error.notFound." + DISH_CODE,
                        new String[]{id.toString(), restaurant.getName()}, "Not found");
            } else if (dish.isDeleted()) {
                errors.rejectValue(
                        "dishes[" + i + "]", "error.deleted." + DISH_CODE,
                        new String[]{dish.getName(), restaurant.getName()}, "Deleted");
            } else if (!dish.isEnabled()) {
                errors.rejectValue(
                        "dishes[" + i + "]", "error.notAvailable." + DISH_CODE,
                        new String[]{dish.getName(), restaurant.getName()}, "Not available");
            }
        }
    }

}
