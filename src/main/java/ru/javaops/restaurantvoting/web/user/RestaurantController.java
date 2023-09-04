package ru.javaops.restaurantvoting.web.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.RESTAURANT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantController {

    public static final String RESTAURANT_URL = "/api/restaurants";

    private RestaurantRepository restaurantRepository;

    @GetMapping()
    public List<Restaurant> getAll() {
        log.info("get all");
        return restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get {}", id);
        return restaurantRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    //TODO: add method security for admin
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant addNew(@RequestBody Restaurant restaurant) {
        log.info("add new {}", restaurant);
        if (restaurant.getId() != null) {
            throw new IllegalArgumentException("restaurant must be new");
        }
        return restaurantRepository.save(restaurant);
    }

    //TODO: add method security for admin
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} to {}", id, restaurant.getName());
        if (restaurantRepository.update(id, restaurant.getName()) != 1) {
            throw new NotFoundException();
        }
    }

    //TODO: add method security for admin
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        if (restaurantRepository.delete(id) == 0) {
            throw new NotFoundException();
        }
    }
}
