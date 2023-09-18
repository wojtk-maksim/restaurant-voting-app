package ru.javaops.restaurantvoting.web.admin_access;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.to.SimpleRestaurant;
import ru.javaops.restaurantvoting.util.Views.Admin;
import ru.javaops.restaurantvoting.web.AbstractRestaurantController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.web.UrlData.*;

@RestController
@RequestMapping(value = AdminRestaurantController.ADMIN_RESTAURANTS_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
public class AdminRestaurantController extends AbstractRestaurantController {

    public static final String ADMIN_RESTAURANTS_URL = API + ADMIN + RESTAURANTS;

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
    public Restaurant addNew(@RequestBody @Valid SimpleRestaurant newRestaurantTo) {
        log.info("add new restaurant {}", newRestaurantTo);
        return restaurantService.add(newRestaurantTo);
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public void update(@PathVariable Long id, @RequestBody @Valid SimpleRestaurant newRestaurantTo) {
        log.info("update restaurant {} to {}", id, newRestaurantTo);
        restaurantService.update(getRestaurantIfExists(id), newRestaurantTo);
    }

    @PatchMapping("/{id}")
    public void enable(@PathVariable Long id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable restaurant {}" : "disable restaurant {}", id);
        restaurantService.enable(getRestaurantIfExists(id), enabled);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("delete restaurant {}", id);
        restaurantService.delete(getRestaurantIfExists(id));
    }

}
