package ru.javaops.restaurantvoting.web.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.service.LunchService;
import ru.javaops.restaurantvoting.util.validation.restaurant.AvailableRestaurant;
import ru.javaops.restaurantvoting.util.validation.restaurant.ValidRestaurant;

import java.time.LocalDate;
import java.util.LinkedHashSet;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.web.UrlData.*;

@RestController
@RequestMapping(value = AdminLunchController.ADMIN_LUNCHES_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminLunchController {

    public static final String ADMIN_LUNCHES_URL = API + ADMIN + LUNCHES;

    private LunchService lunchService;

    @GetMapping("/{date}")
    public Lunch getFromRestaurantOnDate(@PathVariable @ValidRestaurant Long restaurantId, @PathVariable LocalDate date) {
        log.info("get from restaurant {} on {}", restaurantId, date);
        return lunchService.getFromRestaurantOnDate(restaurantId, date);
    }

    @PostMapping
    public void add(@PathVariable Long restaurantId, @RequestBody DateWithDishes dateWithDishes) {
        log.info("add to restaurant {} on {} with dishes {}", restaurantId, dateWithDishes.date(), dateWithDishes.dishes());
        lunchService.add(restaurantId, dateWithDishes.date(), dateWithDishes.dishes());
    }

    @PutMapping("/{date}")
    public void update(@PathVariable @AvailableRestaurant Long restaurantId, @PathVariable LocalDate date, @RequestBody LinkedHashSet<Long> dishIds) {
        log.info("update in restaurant {} on {} for dishes {}", restaurantId, date, dishIds);
        lunchService.update(restaurantId, date, dishIds);
    }

    @PatchMapping("/{date}")
    public void enable(@PathVariable @AvailableRestaurant Long restaurantId, @PathVariable LocalDate date, @RequestParam boolean enabled) {
        log.info(enabled ? "enable lunch in {} on {}" : "disable lunch in restaurant {} on {}", restaurantId, date);
        lunchService.enable(restaurantId, date, enabled);
    }

    @DeleteMapping("/{date}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable @AvailableRestaurant Long restaurantId, @PathVariable LocalDate date) {
        log.info("delete from restaurant {} on {}", restaurantId, date);
        lunchService.delete(restaurantId, date);
    }

    // Used for adding new lunches
    public record DateWithDishes(
            LocalDate date,
            LinkedHashSet<Long> dishes
    ) {
    }

}
