package ru.javaops.restaurantvoting.util.validation.restaurant;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = AvailableRestaurantValidator.class)
@Target({PARAMETER})
@Retention(RUNTIME)
public @interface AvailableRestaurant {

    String message() default "restaurant is not available";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
