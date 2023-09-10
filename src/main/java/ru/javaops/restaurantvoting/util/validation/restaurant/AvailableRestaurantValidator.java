package ru.javaops.restaurantvoting.util.validation.restaurant;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.util.RestaurantUtil;

import static ru.javaops.restaurantvoting.util.validation.ValidationUtil.checkAvailable;
import static ru.javaops.restaurantvoting.util.validation.ValidationUtil.checkExists;

public class AvailableRestaurantValidator implements ConstraintValidator<AvailableRestaurant, Integer> {

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext context) {
        Restaurant restaurant = RestaurantUtil.getRestaurant(id);
        checkExists(restaurant);
        checkAvailable(restaurant);
        return true;
    }

}
