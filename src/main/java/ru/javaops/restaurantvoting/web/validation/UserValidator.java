package ru.javaops.restaurantvoting.web.validation;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javaops.restaurantvoting.config.AuthToken;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.to.user.BasicUserTo;
import ru.javaops.restaurantvoting.to.user.ProfileTo;
import ru.javaops.restaurantvoting.to.user.UpdatedProfileTo;

import static ru.javaops.restaurantvoting.config.SecurityConfig.PASSWORD_ENCODER;

@Component
@Slf4j
@AllArgsConstructor
public class UserValidator implements Validator {

    private UserRepository userRepository;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return BasicUserTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        // Validate if user updates his profile.
        if (target instanceof UpdatedProfileTo updatedData) {
            AuthToken user = (AuthToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.debug("Validate updated data {} for user {}", updatedData, user);
            checkPasswords(user, updatedData, errors);
            if (!user.getName().equals(updatedData.getName())) {
                checkName(updatedData.getName(), errors);
            }
            if (!user.getEmail().equals(updatedData.getEmail())) {
                checkEmail(updatedData.getEmail(), errors);
            }
        }
        // Validate if new user is registering or admin creates or updates another user.
        else if (target instanceof ProfileTo newData) {
            log.debug("Validate new data {}", newData);
            checkName(newData.getName(), errors);
            checkEmail(newData.getEmail(), errors);
        }
    }

    private void checkName(String name, Errors errors) {
        if (userRepository.existsByName(name)) {
            errors.rejectValue("name", "error.alreadyExists.userName");
        }
    }

    private void checkEmail(String email, Errors errors) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            errors.rejectValue("email", "error.alreadyExists.email");
        }
    }

    private void checkPasswords(AuthToken user, UpdatedProfileTo updatedData, Errors errors) {
        String passwordConfirmation = updatedData.getPasswordConfirmation();
        if (passwordConfirmation != null) {
            if (!PASSWORD_ENCODER.matches(passwordConfirmation, userRepository.getPassword(user.getId()))) {
                errors.rejectValue("passwordConfirmation", "error.passwordIncorrect");
            } else if (passwordConfirmation.equals(updatedData.getNewPassword())) {
                errors.rejectValue("newPassword", "error.passwordSameAsPrevious");
            }
        }
    }

}
