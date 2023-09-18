package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.repository.LunchRepository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static ru.javaops.restaurantvoting.util.ValidationUtil.checkDeadline;
import static ru.javaops.restaurantvoting.util.ValidationUtil.checkLunchExists;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class LunchService {

    private LunchRepository lunchRepository;

    private RestaurantService restaurantService;

    private DishService dishService;

    public Lunch getFromRestaurantOnDate(Restaurant restaurant, LocalDate date) {
        log.debug("get lunch from {} on {}", restaurant, date);
        return getLunchIfExists(restaurant, date);
    }

    @Cacheable("lunches")
    public Map<Long, Lunch> getAllOnDate(LocalDate date) {
        log.debug("get all lunches on {}", date);
        return lunchRepository.getAllOnDate(date).stream()
                .collect(toMap(
                        l -> l.getRestaurant().getId(),
                        l -> l,
                        (k, v) -> v,
                        LinkedHashMap::new
                ));
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void add(Restaurant restaurant, LocalDate date, LinkedHashSet<Long> dishIds) {
        log.debug("add lunch to {} on {} with dishes {}", restaurant, date, dishIds);
        checkDeadline(date, "add new lunch");
        if (lunchRepository.existsByDateAndRestaurant(date, restaurant)) {
            throw new IllegalArgumentException("lunch in " + restaurant + " on " + date + " is already provided");
        }
        List<Dish> requestedDishes = getRequestedDishes(restaurant, dishIds);
        lunchRepository.save(new Lunch(date, restaurant, requestedDishes));
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void update(Restaurant restaurant, LocalDate date, LinkedHashSet<Long> dishIds) {
        log.debug("update in {} on {} to new dishes {}", restaurant, date, dishIds);
        Lunch lunch = getLunchIfExists(restaurant, date);
        checkDeadline(date, "update lunch");
        lunch.setDishes(
                getRequestedDishes(restaurant, dishIds)
        );
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void enable(Restaurant restaurant, LocalDate date, boolean enabled) {
        log.debug(enabled ? "enable lunch in {} on {}" : "disable lunch in {} on {}", restaurant, date);
        Lunch lunch = getLunchIfExists(restaurant, date);
        lunch.setEnabled(enabled);
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void delete(Restaurant restaurant, LocalDate date) {
        log.debug("delete lunch from {} on {}", restaurant, date);
        lunchRepository.delete(getLunchIfExists(restaurant, date));
    }

    private Lunch getLunchIfExists(Restaurant restaurant, LocalDate date) {
        Lunch lunch = lunchRepository.getFromRestaurantOnDate(restaurant, date);
        checkLunchExists(lunch, restaurant, date);
        return lunch;
    }

    /*
     * Retrieves dishes from restaurant by their IDs.
     * Checks if dishes are valid (number of retrieved dishes must match number of requested dishes).
     * Checks if dishes are available (it is forbidden to add to lunch unavailable or deleted dishes).
     */
    private List<Dish> getRequestedDishes(Restaurant restaurant, LinkedHashSet<Long> dishIds) {
        Map<Long, Dish> dishesFromRestaurant = dishService.getAllFromRestaurant(restaurant);
        List<Dish> actualDishes = dishIds.stream()
                .map(dishesFromRestaurant::get)
                .toList();
        int numberOfRequestedDishes = dishIds.size();
        if (actualDishes.size() != numberOfRequestedDishes) {
            throw new IllegalArgumentException("invalid dishes");
        }

        List<Dish> availableDishes = actualDishes.stream()
                .filter(d -> d != null && d.isEnabled() && !d.isDeleted())
                .toList();
        if (availableDishes.size() != numberOfRequestedDishes) {
            throw new IllegalArgumentException("you can't add unavailable dishes to lunch");
        }

        return availableDishes;
    }

}
