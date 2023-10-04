package ru.javaops.restaurantvoting.web.admin_access;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.config.AuthToken;
import ru.javaops.restaurantvoting.service.LunchService;
import ru.javaops.restaurantvoting.to.lunch.LunchTo;
import ru.javaops.restaurantvoting.to.lunch.new_data.DishesForSingleLunch;
import ru.javaops.restaurantvoting.web.validation.DishesForLunchValidator;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.web.UrlData.*;

@RestController
@RequestMapping(value = AdminLunchController.ADMIN_LUNCHES_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminLunchController {

    public static final String ADMIN_LUNCHES_URL = API_PATH + ADMIN_PATH + FULL_LUNCHES_PATH;

    private LunchService lunchService;

    private DishesForLunchValidator dishesForLunchValidator;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(dishesForLunchValidator);
    }

    @GetMapping("/{date}")
    public LunchTo getFromRestaurantOnDate(@PathVariable Long restaurantId, @PathVariable LocalDate date) {
        log.info("Get lunch from restaurant {} on {} ", restaurantId, date);
        return lunchService.getFromRestaurantOnDate(restaurantId, date).getRestaurantItem();
    }

    @PostMapping("/{date}")
    public void createOrUpdate(@PathVariable Long restaurantId, @PathVariable LocalDate date,
                               @RequestBody @Valid DishesForSingleLunch dishesForSingleLunch, @AuthenticationPrincipal AuthToken admin) {
        log.info("Create or update lunch in restaurant {} on {} with dishes {} (by admin {})", restaurantId, date, dishesForSingleLunch, admin);
        lunchService.createOrUpdate(restaurantId, date, dishesForSingleLunch.getDishes());
    }

    @PatchMapping("/{date}")
    public void enable(@PathVariable Long restaurantId, @PathVariable LocalDate date,
                       @RequestParam boolean enabled, @AuthenticationPrincipal AuthToken admin) {
        log.info(enabled ?
                        "Enable lunch in restaurant {} on {} (by admin {})" :
                        "Disable lunch in restaurant {} on {} (by admin {})",
                restaurantId, date, admin);
        lunchService.enable(restaurantId, date, enabled);
    }

    // Only SUPER_ADMIN has access.
    @DeleteMapping("/{date}/hard-delete")
    public void delete(@PathVariable Long restaurantId, @PathVariable LocalDate date,
                       @AuthenticationPrincipal AuthToken admin) {
        log.info("Delete lunch from restaurant {} on {} (by admin {})", restaurantId, date, admin);
        lunchService.delete(restaurantId, date);
    }

}
