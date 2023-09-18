package ru.javaops.restaurantvoting.web.public_access;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.util.Views.Public;
import ru.javaops.restaurantvoting.web.AbstractDishController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.util.ValidationUtil.checkDeleted;
import static ru.javaops.restaurantvoting.util.ValidationUtil.checkDishDeleted;
import static ru.javaops.restaurantvoting.web.UrlData.API;
import static ru.javaops.restaurantvoting.web.UrlData.DISHES;

@RestController
@RequestMapping(value = DishController.DISHES_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
public class DishController extends AbstractDishController {

    public static final String DISHES_URL = API + DISHES;

    @GetMapping
    @JsonView(Public.class)
    public List<Dish> getAllFromRestaurant(@PathVariable Long restaurantId) {
        log.info("get all from restaurant {}", restaurantId);
        Restaurant restaurant = getRestaurantIfExists(restaurantId);
        checkDeleted(restaurant);
        return dishService.getAllFromRestaurant(restaurant).values().stream()
                .takeWhile(d -> !d.isDeleted())
                .toList();
    }

    @GetMapping("/{id}")
    @JsonView(Public.class)
    public Dish getFromRestaurant(@PathVariable Long restaurantId, @PathVariable Long id) {
        log.info("get {} from restaurant {}", id, restaurantId);
        Restaurant restaurant = getRestaurantIfExists(restaurantId);
        checkDeleted(restaurant);
        Dish dish = dishService.getAllFromRestaurant(restaurant).get(id);
        checkDishDeleted(dish, restaurant);
        return dish;
    }

}
