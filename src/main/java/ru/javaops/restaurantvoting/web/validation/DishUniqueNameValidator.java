package ru.javaops.restaurantvoting.web.validation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.service.CacheHelper;
import ru.javaops.restaurantvoting.to.DishTo;
import ru.javaops.restaurantvoting.to.RestaurantTo;

import static ru.javaops.restaurantvoting.util.DishUtil.DISH_CODE;
import static ru.javaops.restaurantvoting.util.DishUtil.checkDishFound;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.getRestaurantIfExists;

@Component
@Slf4j
@AllArgsConstructor
public class DishUniqueNameValidator implements Validator {

    private final DishRepository dishRepository;

    private final CacheHelper cacheHelper;

    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return DishTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        String name = ((DishTo) target).getName();

        // Need to retrieve IDs of restaurant and dish. Split request URI by '/':
        // '' / 'api' / 'admin' / 'restaurants' / '{restaurantId}' / 'dishes' / '{dishId}'
        String path = request.getRequestURI();
        String[] pathMembers = path.split("/");
        Long restaurantId = Long.valueOf(pathMembers[4]);
        RestaurantTo restaurant = getRestaurantIfExists(restaurantId);
        log.debug("Validate name {} available for dish in restaurant {}", name, restaurant);

        if (pathMembers.length < 7) {
            // It is POST request to create new dish.
            checkDish(restaurantId, restaurant, name, errors);
        } else {
            // It is PUT request to update dish.
            Long dishId = Long.valueOf(pathMembers[6]);
            DishTo dish = cacheHelper.getAllDishesCachedFromRestaurant(restaurantId).get(dishId);
            checkDishFound(dish, dishId, restaurant);
            if (!name.equals(dish.getName())) {
                checkDish(restaurantId, restaurant, name, errors);
            }
        }
    }

    private void checkDish(Long restaurantId, RestaurantTo restaurant, String name, Errors errors) {
        if (dishRepository.existsByRestaurantIdAndName(restaurantId, name)) {
            errors.rejectValue("name", "error.alreadyExists." + DISH_CODE, new String[]{name, restaurant.getName()}, "");
        }
    }

}
