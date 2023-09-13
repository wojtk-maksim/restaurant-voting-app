package ru.javaops.restaurantvoting.web.admin;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.service.DishService;
import ru.javaops.restaurantvoting.to.dish.NewDishTo;
import ru.javaops.restaurantvoting.util.Views.Admin;
import ru.javaops.restaurantvoting.util.validation.restaurant.ValidRestaurant;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.util.DishUtil.checkDishExists;
import static ru.javaops.restaurantvoting.web.UrlData.*;

@RestController
@RequestMapping(value = AdminDishController.ADMIN_DISHES_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminDishController {

    public static final String ADMIN_DISHES_URL = API + ADMIN + DISHES;

    private DishService dishService;

    @GetMapping
    @JsonView(Admin.class)
    public List<Dish> getAll(@PathVariable @ValidRestaurant Long restaurantId) {
        log.info("get all from restaurant {}", restaurantId);
        return new ArrayList<>(dishService.getAllFromRestaurant(restaurantId).values());
    }

    @GetMapping("/{id}")
    @JsonView(Admin.class)
    public Dish get(@PathVariable @ValidRestaurant Long restaurantId, @PathVariable Long id) {
        log.info("get {} from restaurant {}", id, restaurantId);
        Dish dish = dishService.getAllFromRestaurant(restaurantId).get(id);
        checkDishExists(dish, restaurantId, id);
        return dish;
    }

    @PostMapping
    @JsonView(Admin.class)
    public Dish addNewToRestaurant(@PathVariable Long restaurantId, @RequestBody @Valid NewDishTo newDish) {
        log.info("add new {} to restaurant {}", newDish, restaurantId);
        return dishService.add(restaurantId, newDish);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable @ValidRestaurant Long restaurantId, @PathVariable Long id, @RequestBody NewDishTo simpleDishTo) {
        log.info("update {} for {} in restaurant {}", id, simpleDishTo, restaurantId);
        dishService.update(restaurantId, id, simpleDishTo);
    }

    @PatchMapping("/{id}")
    public void enabled(@PathVariable @ValidRestaurant Long restaurantId, @PathVariable Long id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {} in restaurant {}" : "disable {} in restaurant {}", id, restaurantId);
        dishService.enable(restaurantId, id, enabled);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable @ValidRestaurant Long restaurantId, @PathVariable Long id) {
        log.info("delete dish {} from restaurant {}", id, restaurantId);
        dishService.delete(restaurantId, id);
    }

}
