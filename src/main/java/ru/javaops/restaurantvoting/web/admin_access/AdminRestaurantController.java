package ru.javaops.restaurantvoting.web.admin_access;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.config.AuthToken;
import ru.javaops.restaurantvoting.service.RestaurantService;
import ru.javaops.restaurantvoting.to.RestaurantTo;
import ru.javaops.restaurantvoting.web.validation.RestaurantUniqueNameValidator;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.web.UrlData.*;

@RestController
@RequestMapping(value = AdminRestaurantController.ADMIN_RESTAURANTS_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminRestaurantController {

    public static final String ADMIN_RESTAURANTS_URL = API_PATH + ADMIN_PATH + RESTAURANTS_PATH;

    private RestaurantService restaurantService;

    private RestaurantUniqueNameValidator restaurantValidator;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(restaurantValidator);
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("Get all restaurants");
        return restaurantService.getAll();
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable Long id) {
        log.info("Get restaurant {}", id);
        return restaurantService.get(id);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public RestaurantTo addNew(@RequestBody @Valid RestaurantTo newRestaurant, @AuthenticationPrincipal AuthToken admin) {
        String name = newRestaurant.getName();
        log.info("Add new restaurant [name ='{}'] (by admin {})", name, admin);
        return restaurantService.add(name);
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public void update(@PathVariable Long id, @RequestBody @Valid RestaurantTo updatedRestaurant, @AuthenticationPrincipal AuthToken admin) {
        String name = updatedRestaurant.getName();
        log.info("Update restaurant {} to [name = '{}'] (by admin {})", id, name, admin);
        restaurantService.update(id, name);
    }

    @PatchMapping("/{id}")
    public void enable(@PathVariable Long id, @RequestParam boolean enabled, @AuthenticationPrincipal AuthToken admin) {
        log.info(enabled ? "Enable restaurant {} (by admin {})" : "Disable restaurant {} (by admin {})", id, admin);
        restaurantService.enable(id, enabled);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal AuthToken admin) {
        log.info("Delete restaurant {} (by admin {})", id, admin);
        restaurantService.softDelete(id);
    }

    // Only SUPER_ADMIN has access
    @DeleteMapping("/{id}/hard-delete")
    public void hardDelete(@PathVariable Long id, @AuthenticationPrincipal AuthToken superAdmin) {
        log.info("Hard delete user {} (by admin {})", id, superAdmin);
        restaurantService.hardDelete(id);
    }

}
