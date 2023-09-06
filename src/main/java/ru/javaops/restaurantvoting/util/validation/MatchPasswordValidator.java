package ru.javaops.restaurantvoting.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.web.AuthUser;

import static ru.javaops.restaurantvoting.config.SecurityConfig.PASSWORD_ENCODER;

public class MatchPasswordValidator implements ConstraintValidator<MatchPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        return PASSWORD_ENCODER.matches(password, user.getPassword());
    }
}
