package ru.javaops.restaurantvoting.util.validation.restaurant;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.javaops.restaurantvoting.model.Restaurant;

import static ru.javaops.restaurantvoting.util.RestaurantUtil.getRestaurantIfExists;

public class ValidRestaurantValidator implements ConstraintValidator<ValidRestaurant, Long> {

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        Restaurant restaurant = getRestaurantIfExists(id);
        return true;
    }

}
