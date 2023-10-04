package ru.javaops.restaurantvoting.web.public_access;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.restaurantvoting.Views.Public;
import ru.javaops.restaurantvoting.service.DishService;
import ru.javaops.restaurantvoting.to.DishTo;
import ru.javaops.restaurantvoting.to.RestaurantItemPair;
import ru.javaops.restaurantvoting.to.RestaurantTo;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.util.DishUtil.checkDishDeleted;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.checkRestaurantDeleted;
import static ru.javaops.restaurantvoting.web.UrlData.API_PATH;
import static ru.javaops.restaurantvoting.web.UrlData.FULL_DISHES_PATH;

@RestController
@RequestMapping(value = DishController.DISHES_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class DishController {

    public static final String DISHES_URL = API_PATH + FULL_DISHES_PATH;

    private final DishService dishService;

    @GetMapping
    @JsonView(Public.class)
    public List<DishTo> getAllFromRestaurant(@PathVariable Long restaurantId) {
        log.info("Get all dishes from restaurant {}", restaurantId);
        RestaurantItemPair<List<DishTo>> restaurantItemPair = dishService.getAllFromRestaurant(restaurantId);
        checkRestaurantDeleted(restaurantItemPair.getRestaurant());
        return restaurantItemPair.getRestaurantItem().stream()
                .takeWhile(d -> !d.isDeleted())
                .toList();
    }

    @GetMapping("/{id}")
    @JsonView(Public.class)
    public DishTo getFromRestaurant(@PathVariable Long restaurantId, @PathVariable Long id) {
        log.info("Get dish {} from restaurant {}", id, restaurantId);
        RestaurantItemPair<DishTo> restaurantItemPair = dishService.getFromRestaurant(restaurantId, id);
        RestaurantTo restaurant = restaurantItemPair.getRestaurant();
        checkRestaurantDeleted(restaurant);
        DishTo dish = restaurantItemPair.getRestaurantItem();
        checkDishDeleted(dish, restaurant);
        return dish;
    }

}
