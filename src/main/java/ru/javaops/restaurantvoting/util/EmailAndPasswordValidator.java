package ru.javaops.restaurantvoting.util;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.service.UserService;
import ru.javaops.restaurantvoting.to.user.SimpleUser;
import ru.javaops.restaurantvoting.to.user.UpdatedAccount;
import ru.javaops.restaurantvoting.web.AuthUser;

import static ru.javaops.restaurantvoting.config.SecurityConfig.PASSWORD_ENCODER;

@Component
@AllArgsConstructor
public class EmailAndPasswordValidator implements Validator {

    private UserService userService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return SimpleUser.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        // Validate if user updates his account
        if (target instanceof UpdatedAccount newData) {
            User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

            if (!user.getEmail().equals(newData.getEmail())) {
                checkEmail(newData.getEmail(), errors);
            }

            String currentPassword = newData.getCurrentPassword();
            if (!PASSWORD_ENCODER.matches(currentPassword, user.getPassword())) {
                errors.rejectValue("currentPassword", "user.password", "Password is incorrect");
            }
            if (currentPassword.equals(newData.getNewPassword())) {
                errors.rejectValue("newPassword", "password.same.as.previous", "New password must differ from your current password");
            }
        }

        // Validate if new user is registering or admin creates or updates another user
        else {
            SimpleUser newUser = (SimpleUser) target;
            checkEmail(newUser.getEmail(), errors);
        }
    }

    private void checkEmail(String email, Errors errors) {
        if (userService.isEmailAlreadyRegistered(email)) {
            errors.rejectValue("email", "user.email.occupied", "email is already registered");
        }
    }

}
