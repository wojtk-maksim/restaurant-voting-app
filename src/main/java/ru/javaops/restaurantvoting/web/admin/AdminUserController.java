package ru.javaops.restaurantvoting.web.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.repository.UserRepository;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.util.validation.ValidationUtil.checkExists;
import static ru.javaops.restaurantvoting.web.UrlData.*;

@RestController
@RequestMapping(value = AdminUserController.ADMIN_USERS_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminUserController {

    public static final String ADMIN_USERS_URL = API + ADMIN + USERS;

    private UserRepository userRepository;

    @GetMapping
    public List<User> getAll() {
        log.info("get all");
        return userRepository.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        log.info("get {}", id);
        return checkExists(userRepository.get(id));
    }

    @PatchMapping("/{id}")
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        if (userRepository.enable(id, enabled) == 0) {
            throw new NotFoundException();
        }
    }

}
