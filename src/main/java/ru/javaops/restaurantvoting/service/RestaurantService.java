package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.to.restaurant.NewRestaurantTo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
                .collect(Collectors.toMap(Restaurant::getId, d -> d, (k, v) -> v, LinkedHashMap::new));
    }

    public Restaurant get(long id) {
        log.debug("get {}", id);
        return restaurantRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public Restaurant add(NewRestaurantTo newRestaurantTo) {
        log.debug("add new {}", newRestaurantTo);
        return restaurantRepository.save(new Restaurant(null, newRestaurantTo.name()));
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public void update(long id, NewRestaurantTo newRestaurantTo) {
        log.debug("update {} to {}", id, newRestaurantTo);
        if (restaurantRepository.update(id, newRestaurantTo.name()) == 0) {
            throw new NotFoundException();
        }
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public void enable(long id, boolean enabled) {
        log.debug(enabled ? "enable {}" : "disable {}", id);
        if (restaurantRepository.enable(id, enabled) == 0) {
            throw new NotFoundException();
        }
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public void delete(long id) {
        log.debug("delete {}", id);
        if (restaurantRepository.delete(id) == 0) {
            throw new NotFoundException();
        }
    }
}
