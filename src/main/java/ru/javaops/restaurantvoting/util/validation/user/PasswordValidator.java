package ru.javaops.restaurantvoting.util.validation.user;

import org.springframework.validation.Errors;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.to.user.UpdatedUserTo;
import ru.javaops.restaurantvoting.util.UserUtil;

import static ru.javaops.restaurantvoting.config.SecurityConfig.PASSWORD_ENCODER;

public class PasswordValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UpdatedUserTo.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = UserUtil.getActiveUser().getUser();
        UpdatedUserTo newData = (UpdatedUserTo) target;
        String previousPassword = newData.getOldPassword();
        if (!PASSWORD_ENCODER.matches(previousPassword, user.getPassword())) {
            errors.rejectValue("oldPassword", "password.no.match", "Password is incorrect");
        }
        if (previousPassword.equals(newData.getNewPassword())) {
            errors.rejectValue("newPassword", "password.same.as.previous", "New password must differ from previous");
        }
    }

}
