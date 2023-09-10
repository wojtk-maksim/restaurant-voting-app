package ru.javaops.restaurantvoting.web.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.util.Views.Public;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.util.validation.ValidationUtil.checkExists;
import static ru.javaops.restaurantvoting.web.UrlData.API;
import static ru.javaops.restaurantvoting.web.UrlData.USERS;

@RestController
@RequestMapping(value = UserController.USERS_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class UserController {

    public static final String USERS_URL = API + USERS;

    public static final String DELETED = "DELETED";

    private UserRepository userRepository;

    @GetMapping("/{id}")
    @JsonView(Public.class)
    public User get(@PathVariable int id) {
        log.info("get {}", id);
        User user = checkExists(userRepository.get(id));
        if (user.isDeleted()) {
            return deletedTo(user);
        }
        return user;
    }

    public static User deletedTo(User user) {
        return new User(user.getId(), DELETED, null, null, null, true, null);
    }

}
