package ru.javaops.restaurantvoting.util.validation.restaurant;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.javaops.restaurantvoting.util.RestaurantUtil;

import static ru.javaops.restaurantvoting.util.validation.ValidationUtil.checkExists;

public class ValidRestaurantValidator implements ConstraintValidator<ValidRestaurant, Integer> {

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext context) {
        checkExists(RestaurantUtil.getRestaurant(id));
        return true;
    }

}
