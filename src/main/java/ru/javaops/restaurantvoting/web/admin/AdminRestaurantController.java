package ru.javaops.restaurantvoting.web.admin;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.service.RestaurantService;
import ru.javaops.restaurantvoting.to.restaurant.NewRestaurantTo;
import ru.javaops.restaurantvoting.util.Views.Admin;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.getRestaurantIfExists;
import static ru.javaops.restaurantvoting.web.UrlData.*;

@RestController
@RequestMapping(value = AdminRestaurantController.ADMIN_RESTAURANTS_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminRestaurantController {

    public static final String ADMIN_RESTAURANTS_URL = API + ADMIN + RESTAURANTS;

    private RestaurantService restaurantService;

    @GetMapping
    @JsonView(Admin.class)
    public List<Restaurant> getAll() {
        log.info("get all");
        return new ArrayList<>(restaurantService.getAll().values());
    }

    @GetMapping("/{id}")
    @JsonView(Admin.class)
    public Restaurant get(@PathVariable Long id) {
        log.info("get {}", id);
        return getRestaurantIfExists(id);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @JsonView(Admin.class)
    public Restaurant addNew(@RequestBody @Valid NewRestaurantTo newRestaurantTo) {
        log.info("add new {}", newRestaurantTo);
        return restaurantService.add(newRestaurantTo);
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public void update(@PathVariable Long id, @RequestBody @Valid NewRestaurantTo newRestaurantTo) {
        log.info("update {} to {}", id, newRestaurantTo);
        restaurantService.update(id, newRestaurantTo);
    }

    @PatchMapping("/{id}")
    public void enable(@PathVariable Long id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        restaurantService.enable(id, enabled);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable Long id) {
        log.info("delete {}", id);
        restaurantService.delete(id);
    }

}
