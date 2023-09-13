package ru.javaops.restaurantvoting.util.validation.restaurant;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.util.RestaurantUtil;

import static ru.javaops.restaurantvoting.util.RestaurantUtil.checkRestaurantDeleted;

public class AvailableRestaurantValidator implements ConstraintValidator<AvailableRestaurant, Long> {

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        Restaurant restaurant = RestaurantUtil.getRestaurantIfExists(id);
        checkRestaurantDeleted(restaurant);
        return true;
    }

}
