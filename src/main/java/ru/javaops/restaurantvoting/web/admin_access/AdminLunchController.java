package ru.javaops.restaurantvoting.web.admin_access;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.service.LunchService;
import ru.javaops.restaurantvoting.web.AbstractRestaurantController;

import java.time.LocalDate;
import java.util.LinkedHashSet;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.web.UrlData.*;

@RestController
@RequestMapping(value = AdminLunchController.ADMIN_LUNCHES_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminLunchController extends AbstractRestaurantController {

    public static final String ADMIN_LUNCHES_URL = API + ADMIN + LUNCHES;

    private LunchService lunchService;

    @GetMapping("/{date}")
    public Lunch getFromRestaurantOnDate(@PathVariable Long restaurantId, @PathVariable LocalDate date) {
        log.info("get from restaurant {} on {}", restaurantId, date);
        return lunchService.getFromRestaurantOnDate(getRestaurantIfExists(restaurantId), date);
    }

    @PostMapping("/{date}")
    public void add(@PathVariable Long restaurantId, @PathVariable LocalDate date, @RequestBody LinkedHashSet<Long> dishIds) {
        log.info("add to restaurant {} on {} with dishes {}", restaurantId, date, dishIds);
        lunchService.add(getRestaurantIfExists(restaurantId), date, dishIds);
    }

    @PutMapping("/{date}")
    public void update(@PathVariable Long restaurantId, @PathVariable LocalDate date, @RequestBody LinkedHashSet<Long> dishIds) {
        log.info("update in restaurant {} on {} for dishes {}", restaurantId, date, dishIds);
        lunchService.update(getRestaurantIfExists(restaurantId), date, dishIds);
    }

    @PatchMapping("/{date}")
    public void enable(@PathVariable Long restaurantId, @PathVariable LocalDate date, @RequestParam boolean enabled) {
        log.info(enabled ? "enable lunch in restaurant {} on {}" : "disable lunch in restaurant {} on {}", restaurantId, date);
        lunchService.enable(getRestaurantIfExists(restaurantId), date, enabled);
    }

    @DeleteMapping("/{date}")
    public void delete(@PathVariable Long restaurantId, @PathVariable LocalDate date) {
        log.info("delete from restaurant {} on {}", restaurantId, date);
        lunchService.delete(getRestaurantIfExists(restaurantId), date);
    }

}
