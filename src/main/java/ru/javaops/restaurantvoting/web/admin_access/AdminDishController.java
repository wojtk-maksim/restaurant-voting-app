package ru.javaops.restaurantvoting.web.admin_access;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.config.AuthToken;
import ru.javaops.restaurantvoting.service.DishService;
import ru.javaops.restaurantvoting.to.DishTo;
import ru.javaops.restaurantvoting.web.validation.DishUniqueNameValidator;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.web.UrlData.*;

@RestController
@RequestMapping(value = AdminDishController.ADMIN_DISHES_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminDishController {

    public static final String ADMIN_DISHES_URL = API_PATH + ADMIN_PATH + FULL_DISHES_PATH;

    private DishService dishService;

    private DishUniqueNameValidator dishValidator;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(dishValidator);
    }

    @GetMapping
    public List<DishTo> getAll(@PathVariable Long restaurantId) {
        log.info("Get all dishes from restaurant {}", restaurantId);
        return dishService.getAllFromRestaurant(restaurantId).getRestaurantItem();
    }

    @GetMapping("/{id}")
    public DishTo get(@PathVariable Long restaurantId, @PathVariable Long id) {
        log.info("Get dish {} from restaurant {}", id, restaurantId);
        return dishService.getFromRestaurant(restaurantId, id).getRestaurantItem();
    }

    @PostMapping
    public DishTo addNewToRestaurant(@PathVariable Long restaurantId, @RequestBody @Valid DishTo newDish,
                                     @AuthenticationPrincipal AuthToken admin) {
        String name = newDish.getName();
        int price = newDish.getPrice();
        log.info("Add new dish [name = '{}', price = '{}'] to restaurant {} (by admin {})", name, price, restaurantId, admin);
        return dishService.add(restaurantId, name, price);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long restaurantId, @PathVariable Long id,
                       @RequestBody DishTo updatedDish, @AuthenticationPrincipal AuthToken admin) {
        String name = updatedDish.getName();
        int price = updatedDish.getPrice();
        log.info("Update dish {} to [name = '{}', price ='{}'] in restaurant {} (by admin {})", id, name, price, restaurantId, admin);
        dishService.update(restaurantId, id, name, price);
    }

    @PatchMapping("/{id}")
    public void enabled(@PathVariable Long restaurantId, @PathVariable Long id,
                        @RequestParam boolean enabled, @AuthenticationPrincipal AuthToken admin) {
        log.info(enabled ? "Enable dish {} in restaurant {} (by admin {})" :
                "Disable dish {} in restaurant {} (by admin {})", id, restaurantId, admin);
        dishService.enable(restaurantId, id, enabled);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long restaurantId, @PathVariable Long id,
                       @AuthenticationPrincipal AuthToken admin) {
        log.info("Delete dish {} from restaurant {} (by admin {})", id, restaurantId, admin);
        dishService.softDelete(restaurantId, id);
    }

    // Only SUPER_ADMIN has access
    @DeleteMapping("/{id}/hard-delete")
    public void hardDelete(@PathVariable Long restaurantId, @PathVariable Long id,
                           @AuthenticationPrincipal AuthToken admin) {
        log.info("Hard delete dish {} from restaurant {} (by admin {})", id, restaurantId, admin);
        dishService.hardDelete(restaurantId, id);
    }

}
