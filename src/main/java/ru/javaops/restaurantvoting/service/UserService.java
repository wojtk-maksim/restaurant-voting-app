package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.to.user.NewAccount;
import ru.javaops.restaurantvoting.to.user.UpdatedAccount;

import java.util.List;

import static java.util.Comparator.comparing;
import static ru.javaops.restaurantvoting.util.ValidationUtil.checkUserExists;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<User> getAll() {
        log.debug("get all");
        return userRepository.getAll().stream()
                .sorted(comparing(User::getName)
                        .thenComparing(User::getEmail))
                .toList();
    }

    public User get(long id) {
        log.debug("get {}", id);
        User user = userRepository.get(id);
        checkUserExists(user, id);
        return user;
    }

    @Transactional
    public User register(NewAccount newUser) {
        log.debug("create new user {}", newUser);
        String email = newUser.getEmail();
        return userRepository.save(
                new User(newUser.getName(), email, newUser.getPassword())
        );
    }

    @Transactional
    public User updateAccount(User user, UpdatedAccount updatedData) {
        log.info("user {} updates his account for {}", user.getId(), updatedData);
        user.setName(updatedData.getName());
        user.setEmail(updatedData.getEmail());
        String newPassword = updatedData.getNewPassword();
        if (newPassword != null) {
            user.setPassword(newPassword);
        }
        return userRepository.save(user);
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

    public boolean isEmailAlreadyRegistered(String email) {
        log.debug("check if {} is already registered", email);
        return userRepository.existsByEmail(email);
    }

}
