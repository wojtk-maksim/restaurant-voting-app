package ru.javaops.restaurantvoting.web.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.service.UserService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = AdminUserController.ADMIN_USER_URL, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminUserController {

    public static final String ADMIN_USER_URL = "/api/admin/users";

    UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return userService.get(id);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public User create(@RequestBody User user) {
        if (user.getId() != null) {
            throw new IllegalArgumentException("user must be new");
        }
        return userService.createOrUpdate(user);
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public User update(@RequestBody User user, @PathVariable int id) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("user must have id");
        }
        if (user.getId() != id) {
            throw new IllegalArgumentException("id mismatch");
        }
        return userService.createOrUpdate(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }
}
