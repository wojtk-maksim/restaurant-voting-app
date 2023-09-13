package ru.javaops.restaurantvoting.web.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.service.UserService;
import ru.javaops.restaurantvoting.to.user.NewUserTo;
import ru.javaops.restaurantvoting.to.user.UpdatedUserTo;
import ru.javaops.restaurantvoting.util.Views;
import ru.javaops.restaurantvoting.web.AuthUser;

@RestController
@RequestMapping(value = ProfileController.PROFILE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ProfileController {

    public static final String PROFILE_URL = "/api/profile";

    private UserService userService;

    @GetMapping
    @JsonView(Views.Public.class)
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser);
        return authUser.getUser();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public User create(@RequestBody NewUserTo newUserTo) {
        log.info("register {}", newUserTo);
        return userService.register(newUserTo);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody UpdatedUserTo updatedUserTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} to {}", authUser.getUser().getId(), updatedUserTo);
        userService.updateProfile(authUser.getUser().getId(), updatedUserTo);
    }

    @DeleteMapping
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete {}", authUser.getUser());
        userService.delete(authUser.getUser().getId());
    }

}
