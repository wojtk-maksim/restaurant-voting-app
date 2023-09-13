package ru.javaops.restaurantvoting.service;

import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.to.user.NewUserTo;
import ru.javaops.restaurantvoting.to.user.UpdatedUserTo;

import java.util.List;

import static java.util.Comparator.comparing;
import static ru.javaops.restaurantvoting.config.SecurityConfig.PASSWORD_ENCODER;
import static ru.javaops.restaurantvoting.util.UserUtil.*;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public User get(long id) {
        log.debug("get {}", id);
        User user = userRepository.get(id);
        checkUserExists(user, id);
        return user;
    }

    public List<User> getAll() {
        log.debug("get all");
        return userRepository.getAll().stream()
                .sorted(comparing(User::getName)
                        .thenComparing(User::getEmail))
                .toList();
    }

    @Transactional
    public User register(NewUserTo newUser) {
        log.debug("create {}", newUser);
        String email = newUser.getEmail().toLowerCase();
        checkEmailFound(userRepository.getEmail(email));
        return userRepository.save(
                new User(newUser.getName(), email, encodePassword(newUser.getNewPassword()))
        );
    }

    @Transactional
    public User updateProfile(Long id, UpdatedUserTo updatedData) {
        log.info("update {} to {}", id, updatedData);
        String email = updatedData.getEmail();
        Tuple validationData = userRepository.getValidationDataForUpdate(id, email);
        User user = (User) validationData.get("user");
        if (!email.equals(user.getEmail())) {
            checkEmailFound((String) validationData.get("email"));
        }
        user.setName(updatedData.getName());
        user.setEmail(email);
        if (updatedData.getNewPassword() != null) {
            user.setPassword(PASSWORD_ENCODER.encode(updatedData.getNewPassword()));
        }
        return user;
    }

    @Transactional
    public void enable(Long id, boolean enabled) {
        log.debug(enabled ? "enable {}" : "disable {}", id);
        User user = userRepository.get(id);
        checkUserExists(user, id);
        user.setEnabled(enabled);
    }

    @Transactional
    public void delete(Long id) {
        log.debug("delete with id {}", id);
        User user = userRepository.get(id);
        checkUserExists(user, id);
        user.setDeleted(true);
    }

}
