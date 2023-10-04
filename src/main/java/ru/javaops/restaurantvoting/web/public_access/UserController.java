package ru.javaops.restaurantvoting.web.public_access;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.restaurantvoting.Views.Public;
import ru.javaops.restaurantvoting.to.user.ProfileTo;
import ru.javaops.restaurantvoting.web.AbstractUserController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.web.UrlData.API_PATH;
import static ru.javaops.restaurantvoting.web.UrlData.USERS_PATH;

@RestController
@RequestMapping(value = UserController.USERS_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
public class UserController extends AbstractUserController {

    public static final String USERS_URL = API_PATH + USERS_PATH;

    @GetMapping("/{id}")
    @JsonView(Public.class)
    public ProfileTo get(@PathVariable Long id) {
        log.info("Get user {}", id);
        return userService.get(id);
    }

}
