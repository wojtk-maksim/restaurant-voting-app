package ru.javaops.restaurantvoting.web.public_access;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.to.user.NewAccount;
import ru.javaops.restaurantvoting.to.user.UpdatedAccount;
import ru.javaops.restaurantvoting.util.Views;
import ru.javaops.restaurantvoting.web.AbstractUserController;
import ru.javaops.restaurantvoting.web.AuthUser;

import static ru.javaops.restaurantvoting.web.UrlData.ACCOUNT;
import static ru.javaops.restaurantvoting.web.UrlData.API;

@RestController
@RequestMapping(value = AccountController.ACCOUNT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AccountController extends AbstractUserController {

    public static final String ACCOUNT_URL = API + ACCOUNT;

    @GetMapping
    @JsonView(Views.Public.class)
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser);
        return authUser.getUser();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public User register(@RequestBody @Valid NewAccount newAccount) {
        log.info("register {}", newAccount);
        return userService.register(newAccount);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Valid UpdatedAccount updatedAccount, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} to {}", authUser, updatedAccount);
        userService.updateAccount(authUser.getUser(), updatedAccount);
    }

    @DeleteMapping
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete {}", authUser);
        userService.delete(authUser.getUser().getId());
    }

}
