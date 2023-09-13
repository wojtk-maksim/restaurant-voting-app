package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.to.restaurant.NewRestaurantTo;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.checkRestaurantDeleted;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.checkRestaurantExists;

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
    public Restaurant add(NewRestaurantTo newRestaurant) {
        log.debug("add new {}", newRestaurant);
        return restaurantRepository.save(
                new Restaurant(null, newRestaurant.getName())
        );
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public void update(Long id, NewRestaurantTo newData) {
        log.debug("update {} to {}", id, newData);
        Restaurant restaurant = restaurantRepository.get(id);
        checkRestaurantExists(restaurant, id);
        checkRestaurantDeleted(restaurant);
        restaurant.setName(newData.getName());
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public void enable(long id, boolean enabled) {
        log.debug(enabled ? "enable {}" : "disable {}", id);
        Restaurant restaurant = restaurantRepository.get(id);
        checkRestaurantExists(restaurant, id);
        checkRestaurantDeleted(restaurant);
        restaurant.setEnabled(enabled);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(long id) {
        log.debug("delete {}", id);
        Restaurant restaurant = restaurantRepository.get(id);
        checkRestaurantExists(restaurant, id);
        restaurant.setDeleted(true);
    }

}
