package ru.javaops.restaurantvoting.service;

import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.to.dish.NewDishTo;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static ru.javaops.restaurantvoting.util.DishUtil.checkDishExists;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.checkRestaurantExists;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class DishService {

    RestaurantService restaurantService;

    RestaurantRepository restaurantRepository;

    DishRepository dishRepository;

    @Cacheable(value = "dishes", key = "#restaurantId")
    public Map<Long, Dish> getAllFromRestaurant(Long restaurantId) {
        log.debug("get all from restaurant {}", restaurantId);
        return dishRepository.getAllFromRestaurant(restaurantId).stream()
                .collect(toMap(
                        Dish::getId,
                        d -> d,
                        (k, v) -> v,
                        LinkedHashMap::new)
                );
    }

    public Dish getFromRestaurant(Long restaurantId, Long id) {
        log.debug("get {} from restaurant {}", id, restaurantId);
        Dish dish = dishRepository.get(restaurantId, id);
        checkDishExists(dish, restaurantId, id);
        return dish;
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurantId", allEntries = true)
    public Dish add(Long restaurantId, NewDishTo newDish) {
        log.debug("add new {} to restaurant {}", newDish, restaurantId);

        String name = newDish.getName();
        Tuple validationData = dishRepository.getNewDishValidationData(restaurantId, name);
        Restaurant restaurant = (Restaurant) validationData.get("restaurant");
        checkRestaurantExists(restaurant, restaurantId);

        if (validationData.get("name") != null) {
            throw new IllegalArgumentException("dish with this name already exists");
        }

        return dishRepository.save(
                new Dish(name, newDish.getPrice(), restaurant)
        );
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurantId", allEntries = true)
    public void update(Long restaurantId, Long id, NewDishTo newDish) {
        log.debug("update {} id in restaurant {} to {}", id, restaurantId, newDish);

        String name = newDish.getName();
        Tuple validationData = dishRepository.getUpdatedDishValidationData(id, name);

        Dish dish = (Dish) validationData.get("dish");
        checkDishExists(dish, restaurantId, id);

        if (validationData.get("name") != null && !dish.getName().equals(name)) {
            throw new IllegalArgumentException("dish with this name already exists");
        }

        dish.setName(newDish.getName());
        dish.setPrice(newDish.getPrice());
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurantId", allEntries = true)
    public void enable(Long restaurantId, Long id, boolean enabled) {
        log.debug(enabled ? "enable {} in restaurant {}" : "disable {} in restaurant {}", id, restaurantId);
        Dish dish = dishRepository.get(restaurantId, id);
        checkDishExists(dish, restaurantId, id);
        dish.setEnabled(enabled);
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurantId", allEntries = true)
    public void delete(Long restaurantId, Long id) {
        log.debug("delete {} from restaurant {}", id, restaurantId);
        Dish dish = dishRepository.get(restaurantId, id);
        checkDishExists(dish, restaurantId, id);
        dish.setDeleted(true);
    }

}
