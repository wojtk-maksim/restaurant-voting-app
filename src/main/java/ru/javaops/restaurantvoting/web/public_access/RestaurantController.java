package ru.javaops.restaurantvoting.web.public_access;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.restaurantvoting.Views.Public;
import ru.javaops.restaurantvoting.service.RestaurantService;
import ru.javaops.restaurantvoting.to.RestaurantTo;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.checkRestaurantDeleted;
import static ru.javaops.restaurantvoting.web.UrlData.API_PATH;
import static ru.javaops.restaurantvoting.web.UrlData.RESTAURANTS_PATH;


@RestController
@RequestMapping(value = RestaurantController.RESTAURANTS_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantController {

    public static final String RESTAURANTS_URL = API_PATH + RESTAURANTS_PATH;

    private final RestaurantService restaurantService;

    @GetMapping
    @JsonView(Public.class)
    public List<RestaurantTo> getAll() {
        log.info("Get all restaurants");
        return restaurantService.getAll().stream()
                .takeWhile(r -> !r.isDeleted())
                .toList();
    }

    @GetMapping("/{id}")
    @JsonView(Public.class)
    public RestaurantTo get(@PathVariable Long id) {
        log.info("Get restaurant {}", id);
        RestaurantTo restaurant = restaurantService.get(id);
        checkRestaurantDeleted(restaurant);
        return restaurant;
    }

}
