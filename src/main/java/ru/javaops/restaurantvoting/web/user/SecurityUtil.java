package ru.javaops.restaurantvoting.web.user;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.repository.UserRepository;

@Component
@AllArgsConstructor
public class SecurityUtil {

    @Autowired
    private UserRepository bean;

    private static UserRepository userRepository;

    private static int authorizedUserId = 2;

    @PostConstruct
    public void init() {
        userRepository = bean;
    }

    public static User authId() {
        return userRepository.findById(authorizedUserId).orElseThrow(NotFoundException::new);
    }
}
