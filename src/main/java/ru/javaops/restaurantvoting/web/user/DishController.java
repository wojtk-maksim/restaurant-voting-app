package ru.javaops.restaurantvoting.web.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.service.DishService;
import ru.javaops.restaurantvoting.util.Views.Public;
import ru.javaops.restaurantvoting.util.validation.restaurant.ValidRestaurant;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javaops.restaurantvoting.util.DishUtil.checkDishDeleted;
import static ru.javaops.restaurantvoting.util.DishUtil.checkDishExists;
import static ru.javaops.restaurantvoting.web.UrlData.API;
import static ru.javaops.restaurantvoting.web.UrlData.DISHES;

@RestController
@RequestMapping(value = DishController.DISHES_URL, produces = APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class DishController {

    public static final String DISHES_URL = API + DISHES;

    private DishService dishService;

    @GetMapping
    @JsonView(Public.class)
    public List<Dish> getAllFromRestaurant(@PathVariable @ValidRestaurant Long restaurantId) {
        log.info("get all from restaurant {}", restaurantId);
        return dishService.getAllFromRestaurant(restaurantId).values().stream()
                .takeWhile(d -> !d.isDeleted())
                .toList();
    }

    @GetMapping("/{id}")
    @JsonView(Public.class)
    public Dish getFromRestaurant(@PathVariable @ValidRestaurant Long restaurantId, @PathVariable Long id) {
        log.info("get {} from restaurant {}", id, restaurantId);
        Dish dish = dishService.getAllFromRestaurant(restaurantId).get(id);
        checkDishExists(dish, restaurantId, id);
        checkDishDeleted(dish, restaurantId, id);
        return dish;
    }

}
