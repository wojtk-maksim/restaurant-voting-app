package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.error.EmailOccupiedException;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.to.user.new_data.NewUserTo;
import ru.javaops.restaurantvoting.to.user.new_data.UpdatedUserTo;
import ru.javaops.restaurantvoting.to.user.registered.UserProfileTo;

import static ru.javaops.restaurantvoting.config.SecurityConfig.PASSWORD_ENCODER;
import static ru.javaops.restaurantvoting.to.ToConverter.registeredUserTo;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public UserProfileTo get(int id) {
        log.info("get {}", id);
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        return registeredUserTo(user);
    }

    @Transactional
    public UserProfileTo create(NewUserTo newUser) {
        log.info("create {}", newUser);
        if (userRepository.emailExists(newUser.getEmail())) {
            throw new EmailOccupiedException();
        }
        return registeredUserTo(userRepository.save(
                new User(newUser.getName(), newUser.getEmail().toLowerCase(), PASSWORD_ENCODER.encode(newUser.getPassword())))
        );
    }

    @Transactional
    public void update(User user, UpdatedUserTo updatedUserTo) {
        log.info("update {} to {}", user.getId(), updatedUserTo);
        String email = updatedUserTo.getEmail().toLowerCase();
        if (!email.equals(user.getEmail()) && userRepository.emailExists(email)) {
            throw new EmailOccupiedException();
        }
        if (userRepository.update(
                user.getId(),
                updatedUserTo.getName(),
                email,
                updatedUserTo.getPassword() == null ?
                        user.getPassword() :
                        PASSWORD_ENCODER.encode(updatedUserTo.getPassword()))
                != 1) {
            throw new NotFoundException();
        }
    }

    @Transactional
    public void delete(int id) {
        log.info("delete with id {}", id);
        if (userRepository.delete(id) != 1) {
            throw new NotFoundException();
        }
    }
}
