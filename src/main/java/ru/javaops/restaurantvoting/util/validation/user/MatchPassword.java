package ru.javaops.restaurantvoting.util.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = MatchPasswordValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface MatchPassword {

    String message() default "password doesn't match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
