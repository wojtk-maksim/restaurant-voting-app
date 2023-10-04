package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.to.user.AuthTo;
import ru.javaops.restaurantvoting.to.user.ProfileTo;

import java.util.Set;

import static ru.javaops.restaurantvoting.config.SecurityConfig.PASSWORD_ENCODER;
import static ru.javaops.restaurantvoting.model.Role.BANNED;
import static ru.javaops.restaurantvoting.model.Role.DELETED;
import static ru.javaops.restaurantvoting.util.UserUtil.*;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class UserService {

    public static final String DELETED_USER_NAME = "DELETED";

    private UserRepository userRepository;

    public User getForAuthentication(AuthTo authUser) {
        String email = authUser.getEmail().toLowerCase();
        User user = userRepository.getByEmail(email);
        log.debug("Authenticate user {}", user);
        checkUsernameFound(user, email);
        checkBannedOrDeleted(user);
        if (!PASSWORD_ENCODER.matches(authUser.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(messageSourceAccessor.getMessage("error.passwordIncorrect"));
        }
        return user;
    }

    /* ***************************************************************************
     *  Workaround for JwtFilter to deny access for banned or deleted user
     *  (in case his token has not expired yet). Also used during authentication.
     *  See UserUtil.checkBannedOrDeleted()
     */
    @Cacheable("banned")
    public Set<Long> getAllBanned() {
        log.debug("Get IDs of banned users");
        return userRepository.getAllBanned();
    }

    @Cacheable("deleted")
    public Set<Long> getAllDeleted() {
        log.debug("Get IDs of deleted users");
        return userRepository.getAllDeleted();
    }
    // ****************************************************************************

    public ProfileTo get(long id) {
        log.debug("Get user {}", id);
        User user = userRepository.get(id);
        checkUserFound(user, id);
        return profileTo(user);
    }

    @Transactional
    public ProfileTo create(String name, String email, String password) {
        // Not checking if email or name are already registered. Rely on UserValidator.
        log.debug("Register new user [name = '{}', email = '{}']", name, email);
        email = email.toLowerCase();
        password = PASSWORD_ENCODER.encode(password);
        return profileTo(userRepository.save(new User(name, email, password)));
    }

    @Transactional
    public void update(Long id, String name, String email, String password) {
        // Not checking if email or name are already registered. Rely on UserValidator.
        log.debug("Update user {} [name = '{}', email = '{}']", id, name, email);
        email = email.toLowerCase();
        // Password can be null if user updates his account without changing the password.
        if (password == null) {
            checkUserFound(userRepository.updateWithoutPassword(id, name, email), id);
        } else {
            password = PASSWORD_ENCODER.encode(password);
            checkUserFound(userRepository.update(id, name, email, password), id);
        }
    }

    @Transactional
    @CacheEvict(value = "banned", allEntries = true)
    public void enable(Long id, boolean enabled) {
        log.debug(enabled ? "Enable user {}" : "Disable user {}", id);
        checkUserFound(userRepository.enable(id, enabled), id);
    }

    @Transactional
    @CacheEvict(value = "deleted", allEntries = true)
    public void softDelete(Long id) {
        log.debug("Delete user {}", id);
        checkUserFound(userRepository.softDelete(id), id);
    }

    @Transactional
    @CacheEvict(value = {"banned", "deleted"}, allEntries = true)
    public void hardDelete(Long id) {
        log.debug("Hard delete user {}", id);
        checkUserFound(userRepository.hardDelete(id), id);
    }

    public static ProfileTo profileTo(User user) {
        if (user.isDeleted()) {
            return new ProfileTo(user.getId(), DELETED_USER_NAME, null, null, null, DELETED);
        }
        return new ProfileTo(user.getId(), user.getName(), user.getEmail(), null, user.getRegistered(), user.isEnabled() ? user.getRole() : BANNED);
    }

}
