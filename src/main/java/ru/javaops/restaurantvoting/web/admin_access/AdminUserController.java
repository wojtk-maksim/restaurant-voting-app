package ru.javaops.restaurantvoting.web.admin_access;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.service.UserService;
import ru.javaops.restaurantvoting.util.Views.Admin;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.web.UrlData.*;

@RestController
@RequestMapping(value = AdminUserController.ADMIN_USERS_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminUserController {

    public static final String ADMIN_USERS_URL = API + ADMIN + USERS;

    private UserService userService;

    @GetMapping
    @JsonView(Admin.class)
    public List<User> getAll() {
        log.info("get all");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    @JsonView(Admin.class)
    public User get(@PathVariable int id) {
        log.info("get {}", id);
        return userService.get(id);
    }

    @PatchMapping("/{id}")
    public void enable(@PathVariable Long id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        userService.enable(id, enabled);
    }

}
