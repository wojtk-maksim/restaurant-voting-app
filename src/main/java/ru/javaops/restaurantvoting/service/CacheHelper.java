package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.to.DishTo;
import ru.javaops.restaurantvoting.to.RestaurantTo;
import ru.javaops.restaurantvoting.to.lunch.CachedLunchObject;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
@AllArgsConstructor
public class CacheHelper {

    private final RestaurantRepository restaurantRepository;

    private final DishRepository dishRepository;

    private final LunchRepository lunchRepository;

    @Cacheable("restaurants")
    public Map<Long, RestaurantTo> getRestaurantsCached() {
        log.debug("Get all restaurants");
        return restaurantRepository.getAll().stream()
                .collect(toMap(Restaurant::getId, RestaurantService::restaurantTo, (k, v) -> v, LinkedHashMap::new));
    }

    @Cacheable(value = "dishes")
    public Map<Long, DishTo> getAllDishesCachedFromRestaurant(Long restaurantId) {
        log.debug("Get all dishes from restaurant {}", restaurantId);
        return dishRepository.getAllFromRestaurant(restaurantId).stream()
                .collect(toMap(Dish::getId, DishService::dishTo, (k, v) -> v, LinkedHashMap::new));
    }

    @Cacheable("lunches")
    public Map<Long, CachedLunchObject> getCachedLunchesOnDate(LocalDate date) {
        log.debug("Get all lunches on {}", date);
        return lunchRepository.getAllOnDateForCache(date).stream()
                .collect(toMap(
                        l -> l.getRestaurant().getId(),
                        l -> {
                            List<Long> dishIds = l.getDishes().stream()
                                    .map(Dish::getId)
                                    .toList();
                            return new CachedLunchObject(l.getId(), dishIds, l.isEnabled());
                        },
                        (v1, v2) -> v2,
                        LinkedHashMap::new
                ));
    }

    @CacheEvict(value = "lunches", allEntries = true)
    @Scheduled(fixedRateString = "${caching.lunchesTTL}")
    public void clearLunchCache() {
        log.debug("Clear lunch cache");
    }

}
