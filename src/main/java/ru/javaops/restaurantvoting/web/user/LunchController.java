package ru.javaops.restaurantvoting.web.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.service.LunchService;
import ru.javaops.restaurantvoting.util.validation.restaurant.ValidRestaurant;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.web.UrlData.API;
import static ru.javaops.restaurantvoting.web.UrlData.LUNCHES;

@RestController
@RequestMapping(value = LunchController.LUNCHES_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class LunchController {

    public static final String LUNCHES_URL = API + LUNCHES;

    private LunchService lunchService;

    @GetMapping("/{date}")
    public Lunch getFromRestaurantOnDate(@PathVariable @ValidRestaurant Long restaurantId, @PathVariable LocalDate date) {
        log.info("get from restaurant {} on {}", restaurantId, date);
        return lunchService.getFromRestaurantOnDate(restaurantId, date);
    }

}
