package ru.javaops.restaurantvoting.web.public_access;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.config.AuthToken;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.to.user.AuthTo;
import ru.javaops.restaurantvoting.to.user.ProfileTo;
import ru.javaops.restaurantvoting.to.user.UpdatedProfileTo;
import ru.javaops.restaurantvoting.web.AbstractUserController;
import ru.javaops.restaurantvoting.web.UrlData;

import static ru.javaops.restaurantvoting.util.JwtUtil.generateToken;
import static ru.javaops.restaurantvoting.web.UrlData.API_PATH;

@RestController
@RequestMapping(value = ProfileController.PROFILE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ProfileController extends AbstractUserController {

    public static final String PROFILE_URL = API_PATH + UrlData.PROFILE_PATH;

    @GetMapping
    public ProfileTo get(@AuthenticationPrincipal AuthToken authUser) {
        log.info("Get profile of user {}", authUser);
        return userService.get(authUser.getId());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProfileTo register(@RequestBody @Valid ProfileTo newUser) {
        log.info("Register new user {}", newUser);
        return userService.create(newUser.getName(), newUser.getEmail(), newUser.getPassword());
    }

    @PostMapping(value = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
    // Use @RequestParam, otherwise UserValidator throws not valid target exception.
    // Couldn't manage it to validate only specific @RequestBody parameters.
    public String authenticate(@RequestBody @Valid AuthTo authUser) {
        log.info("Authenticate {}", authUser);
        User user = userService.getForAuthentication(authUser);
        return generateToken(user);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Valid UpdatedProfileTo updatedUser, @AuthenticationPrincipal AuthToken user) {
        log.info("Update profile of user {} to {}", user, updatedUser);
        userService.update(user.getId(), updatedUser.getName(), updatedUser.getEmail(), updatedUser.getNewPassword());
    }

    @DeleteMapping
    public void delete(@AuthenticationPrincipal AuthToken user) {
        log.info("Delete profile of user {}", user);
        userService.softDelete(user.getId());
    }

}
