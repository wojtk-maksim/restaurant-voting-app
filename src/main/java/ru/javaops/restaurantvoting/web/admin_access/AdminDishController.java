package ru.javaops.restaurantvoting.web.admin_access;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.to.NewDishTo;
import ru.javaops.restaurantvoting.util.Views.Admin;
import ru.javaops.restaurantvoting.web.AbstractDishController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.web.UrlData.*;

@RestController
@RequestMapping(value = AdminDishController.ADMIN_DISHES_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
public class AdminDishController extends AbstractDishController {

    public static final String ADMIN_DISHES_URL = API + ADMIN + DISHES;

    @GetMapping
    @JsonView(Admin.class)
    public List<Dish> getAll(@PathVariable Long restaurantId) {
        log.info("get all from restaurant {}", restaurantId);
        Restaurant restaurant = getRestaurantIfExists(restaurantId);
        return new ArrayList<>(dishService.getAllFromRestaurant(restaurant).values());
    }

    @GetMapping("/{id}")
    @JsonView(Admin.class)
    public Dish get(@PathVariable Long restaurantId, @PathVariable Long id) {
        log.info("get {} from restaurant {}", id, restaurantId);
        Restaurant restaurant = getRestaurantIfExists(restaurantId);
        return getDishIfExists(restaurant, id);
    }

    @PostMapping
    @JsonView(Admin.class)
    public Dish addNewToRestaurant(@PathVariable Long restaurantId, @RequestBody @Valid NewDishTo newDish) {
        log.info("add new {} to restaurant {}", newDish, restaurantId);
        Restaurant restaurant = getRestaurantIfExists(restaurantId);
        return dishService.add(restaurant, newDish);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long restaurantId, @PathVariable Long id, @RequestBody NewDishTo simpleDishTo) {
        log.info("update {} for {} in restaurant {}", id, simpleDishTo, restaurantId);
        Restaurant restaurant = getRestaurantIfExists(restaurantId);
        dishService.update(restaurant, getDishIfExists(restaurant, id), simpleDishTo);
    }

    @PatchMapping("/{id}")
    public void enabled(@PathVariable Long restaurantId, @PathVariable Long id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {} in restaurant {}" : "disable {} in restaurant {}", id, restaurantId);
        Restaurant restaurant = getRestaurantIfExists(restaurantId);
        dishService.enable(restaurant, getDishIfExists(restaurant, id), enabled);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable Long restaurantId, @PathVariable Long id) {
        log.info("delete dish {} from restaurant {}", id, restaurantId);
        Restaurant restaurant = getRestaurantIfExists(restaurantId);
        dishService.delete(restaurant, getDishIfExists(restaurant, id));
    }

}
