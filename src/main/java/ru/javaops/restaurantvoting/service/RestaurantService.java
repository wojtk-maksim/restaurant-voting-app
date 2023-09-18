package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.to.SimpleRestaurant;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static ru.javaops.restaurantvoting.util.ValidationUtil.checkRestaurantExists;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class RestaurantService {

    private RestaurantRepository restaurantRepository;

    @Cacheable("restaurants")
    public Map<Long, Restaurant> getAll() {
        log.debug("get all");
        return restaurantRepository.getAll().stream()
                .collect(toMap(
                        Restaurant::getId,
                        r -> r,
                        (k, v) -> v,
                        LinkedHashMap::new)
                );
    }

    public Restaurant get(Long id) {
        log.debug("get {}", id);
        Restaurant restaurant = restaurantRepository.get(id);
        checkRestaurantExists(restaurant, id);
        return restaurant;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public Restaurant add(SimpleRestaurant newRestaurant) {
        log.debug("add new {}", newRestaurant);
        return restaurantRepository.save(new Restaurant(newRestaurant.getName()));
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public void update(Restaurant restaurant, SimpleRestaurant updatedRestaurant) {
        log.debug("update {} to {}", restaurant, updatedRestaurant);
        restaurant.setName(updatedRestaurant.getName());
        restaurantRepository.save(restaurant);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public void enable(Restaurant restaurant, boolean enabled) {
        log.debug(enabled ? "enable {}" : "disable {}", restaurant);
        restaurant.setEnabled(enabled);
        restaurantRepository.save(restaurant);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(Restaurant restaurant) {
        log.debug("delete {}", restaurant);
        restaurant.setDeleted(true);
        restaurantRepository.save(restaurant);
    }

}
