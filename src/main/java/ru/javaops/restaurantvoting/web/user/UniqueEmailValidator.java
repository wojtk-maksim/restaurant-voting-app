package ru.javaops.restaurantvoting.web.user;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.to.user.new_data.AbstractNewUserTo;
import ru.javaops.restaurantvoting.web.AuthUser;

@Component
@AllArgsConstructor
public class UniqueEmailValidator implements org.springframework.validation.Validator {

    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AbstractNewUserTo.class);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        String email = ((AbstractNewUserTo) target).getEmail();
        if (StringUtils.hasText(email)) {
            if (userRepository.emailExists(email)) {
                AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (authUser == null || !email.equalsIgnoreCase(authUser.getUsername())) {
                    errors.rejectValue("email", "email.occupied", "email is occupied");
                }
            }
        }
    }
}
