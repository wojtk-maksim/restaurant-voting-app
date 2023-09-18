package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.to.NewDishTo;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static ru.javaops.restaurantvoting.util.ValidationUtil.checkDishExists;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class DishService {

    DishRepository dishRepository;

    @Cacheable(value = "dishes", key = "#restaurant")
    public Map<Long, Dish> getAllFromRestaurant(Restaurant restaurant) {
        log.debug("get all from {}", restaurant);
        return dishRepository.getAllFromRestaurant(restaurant).stream()
                .collect(toMap(
                        Dish::getId,
                        d -> d,
                        (k, v) -> v,
                        LinkedHashMap::new)
                );
    }

    public Dish getFromRestaurant(Restaurant restaurant, Long id) {
        log.debug("get {} from {}", id, restaurant);
        Dish dish = dishRepository.get(restaurant, id);
        checkDishExists(dish, id, restaurant);
        return dish;
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurant", allEntries = true)
    public Dish add(Restaurant restaurant, NewDishTo newDish) {
        log.debug("add new dish {} to {}", newDish, restaurant);
        String name = newDish.getName();
        checkUniqueName(restaurant, name);
        return dishRepository.save(new Dish(name, newDish.getPrice(), restaurant));
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurant", allEntries = true)
    public void update(Restaurant restaurant, Dish dish, NewDishTo updatedData) {
        log.debug("update {} to {} in {}", dish, updatedData, restaurant);
        String newName = updatedData.getName();
        if (!newName.equals(dish.getName())) {
            checkUniqueName(restaurant, newName);
            dish.setName(newName);
        }
        dish.setPrice(updatedData.getPrice());
        dishRepository.save(dish);
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurant", allEntries = true)
    public void enable(Restaurant restaurant, Dish dish, boolean enabled) {
        log.debug(enabled ? "enable {} in {}" : "disable {} in {}", dish, restaurant);
        dish.setEnabled(enabled);
        dishRepository.save(dish);
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurant", allEntries = true)
    public void delete(Restaurant restaurant, Dish dish) {
        log.debug("delete {} from {}", dish, restaurant);
        dish.setDeleted(true);
        dishRepository.save(dish);
    }

    private void checkUniqueName(Restaurant restaurant, String name) {
        if (dishRepository.existsByRestaurantAndName(restaurant, name)) {
            throw new IllegalArgumentException("dish with this name already exists");
        }
    }

}
