package ru.javaops.restaurantvoting.web.admin_access;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.config.AuthToken;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.to.user.ProfileTo;
import ru.javaops.restaurantvoting.web.AbstractUserController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.config.SecurityConfig.PASSWORD_ENCODER;
import static ru.javaops.restaurantvoting.util.UserUtil.checkUserFound;
import static ru.javaops.restaurantvoting.web.UrlData.*;

@RestController
@RequestMapping(value = AdminUserController.ADMIN_USERS_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminUserController extends AbstractUserController {

    public static final String ADMIN_USERS_URL = API_PATH + ADMIN_PATH + USERS_PATH;

    private final UserRepository userRepository;

    @GetMapping
    public List<User> getAll() {
        log.info("Get all users");
        return userRepository.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        log.info("Get user {}", id);
        User user = userRepository.get(id);
        checkUserFound(user, id);
        return user;
    }

    @PostMapping
    public User create(@RequestBody @Valid ProfileTo newUser, @AuthenticationPrincipal AuthToken admin) {
        String name = newUser.getName();
        String email = newUser.getEmail();
        log.info("Create new user [name = '{}', email = '{}'] (by admin {})", name, email, admin);
        return userRepository.save(new User(name, email.toLowerCase(), PASSWORD_ENCODER.encode(newUser.getPassword())));
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody @Valid ProfileTo updatedUser,
                       @AuthenticationPrincipal AuthToken admin) {
        String name = updatedUser.getName();
        String email = updatedUser.getEmail();
        log.info("Update user {} to [name = '{}', email = '{}'] (by admin {})", id, name, email, admin);
        userService.update(id, name, email, updatedUser.getPassword());
    }

    @PatchMapping("/{id}")
    public void enable(@PathVariable Long id, @RequestParam boolean enabled, @AuthenticationPrincipal AuthToken admin) {
        log.info(enabled ? "Enable user {} (by admin {})" : "Disable user {} (by admin {})", id, admin);
        userService.enable(id, enabled);
    }

    @DeleteMapping("/{id}")
    public void softDelete(@PathVariable Long id, @AuthenticationPrincipal AuthToken admin) {
        log.info("Soft delete user {} by admin {}", id, admin);
        userService.softDelete(id);
    }

    // Only SUPER_ADMIN has access
    @DeleteMapping("/{id}/hard-delete")
    public void hardDelete(@PathVariable Long id, @AuthenticationPrincipal AuthToken superAdmin) {
        log.info("Hard delete user {} (by admin {})", id, superAdmin);
        userService.hardDelete(id);
    }

}
