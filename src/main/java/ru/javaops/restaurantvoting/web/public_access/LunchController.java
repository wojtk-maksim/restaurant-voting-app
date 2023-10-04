package ru.javaops.restaurantvoting.web.public_access;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.restaurantvoting.service.LunchService;
import ru.javaops.restaurantvoting.to.lunch.LunchTo;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.web.UrlData.API_PATH;
import static ru.javaops.restaurantvoting.web.UrlData.FULL_LUNCHES_PATH;

@RestController
@RequestMapping(value = LunchController.LUNCHES_URL, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class LunchController {

    public static final String LUNCHES_URL = API_PATH + FULL_LUNCHES_PATH;

    private final LunchService lunchService;

    @GetMapping("/{date}")
    public LunchTo getFromRestaurantOnDate(@PathVariable Long restaurantId, @PathVariable LocalDate date) {
        log.info("Get lunch from restaurant {} on {}", restaurantId, date);
        return lunchService.getFromRestaurantOnDate(restaurantId, date).getRestaurantItem();
    }

}
