package ru.javaops.restaurantvoting.web.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.service.RestaurantService;
import ru.javaops.restaurantvoting.util.Views.Public;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.checkRestaurantDeleted;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.getRestaurantIfExists;
import static ru.javaops.restaurantvoting.web.UrlData.API;
import static ru.javaops.restaurantvoting.web.UrlData.RESTAURANTS;


@RestController
@RequestMapping(value = RestaurantController.RESTAURANTS_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantController {

    public static final String RESTAURANTS_URL = API + RESTAURANTS;

    private RestaurantService restaurantService;

    @GetMapping
    @JsonView(Public.class)
    public List<Restaurant> getAll() {
        log.info("get all");
        return restaurantService.getAll().values().stream()
                .takeWhile(r -> !r.isDeleted())
                .toList();
    }

    @GetMapping("/{id}")
    @JsonView(Public.class)
    public Restaurant get(@PathVariable Long id) {
        log.info("get {}", id);
        Restaurant restaurant = getRestaurantIfExists(id);
        checkRestaurantDeleted(restaurant);
        return restaurant;
    }

}
