package ru.javaops.restaurantvoting.web.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.service.DishService;
import ru.javaops.restaurantvoting.to.RestaurantDishTo;
import ru.javaops.restaurantvoting.to.SimpleDishTo;

import java.util.List;

@RestController
@RequestMapping(value = DishController.DISH_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class DishController {

    public static final String DISH_URL = RestaurantController.RESTAURANT_URL + "/{restaurantId}/dishes";

    private DishRepository dishRepository;

    private DishService dishService;

    @GetMapping
    public List<RestaurantDishTo> getAllFromRestaurant(@PathVariable int restaurantId) {
        log.info("get all from restaurant {}", restaurantId);
        return dishRepository.getAllFromRestaurant(restaurantId);
    }

    @GetMapping("/{id}")
    public RestaurantDishTo getFromRestaurant(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get {} from restaurant {}", id, restaurantId);
        return dishRepository.getRestaurantDishTo(restaurantId, id).orElseThrow(NotFoundException::new);
    }

    //TODO: add method security
    @PostMapping
    public RestaurantDishTo addNew(@PathVariable int restaurantId, @RequestBody SimpleDishTo simpleDishTo) {
        log.info("add new {} for restaurant {}", simpleDishTo, restaurantId);
        return dishService.add(simpleDishTo, restaurantId);
    }

    //TODO: add method security
    @PutMapping("/{id}")
    public void update(@PathVariable int restaurantId, @PathVariable int id, @RequestBody SimpleDishTo simpleDishTo) {
        log.info("update {} for {} in restaurant {}", id, simpleDishTo, restaurantId);
        dishService.update(restaurantId, id, simpleDishTo);
    }

    //TODO: add method security
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete {} from restaurant {}", id, restaurantId);
        if (dishRepository.delete(restaurantId, id) == 0) {
            throw new NotFoundException();
        }
    }
}
