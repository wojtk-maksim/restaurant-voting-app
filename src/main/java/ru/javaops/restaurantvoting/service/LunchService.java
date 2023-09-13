package ru.javaops.restaurantvoting.service;

import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static ru.javaops.restaurantvoting.util.LunchUtil.checkLunchExists;
import static ru.javaops.restaurantvoting.util.validation.ValidationUtil.checkDeadline;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class LunchService {

    private RestaurantService restaurantService;

    private RestaurantRepository restaurantRepository;

    private LunchRepository lunchRepository;

    private DishRepository dishRepository;


    public Lunch getFromRestaurantOnDate(Long restaurantId, LocalDate date) {
        log.debug("get lunch from restaurant {} on {}", restaurantId, date);
        Lunch lunch = lunchRepository.getFromRestaurantOnDate(restaurantId, date);
        checkLunchExists(lunch, restaurantId, date);
        return lunch;
    }

    @Cacheable("lunches")
    public List<Lunch> getAllOnDate(LocalDate date) {
        log.debug("get all on {}", date);
        return lunchRepository.getAllOnDate(date);
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void add(Long restaurantId, LocalDate date, LinkedHashSet<Long> dishIds) {
        log.debug("add lunch to restaurant {} on {} with dishes {}", restaurantId, date, dishIds);

        Tuple lunchValidationTuple = restaurantRepository.getLunchValidationTuple(restaurantId, date);

        Restaurant restaurant = (Restaurant) lunchValidationTuple.get("restaurant");

        checkDeadline(date, "add new lunch");

        if (lunchValidationTuple.get("lunch") != null) {
            throw new IllegalArgumentException("restaurant " + restaurantId + " already has lunch on " + date);
        }

        List<Dish> requestedDishes = getRequestedDishes(restaurantId, dishIds);

        lunchRepository.save(new Lunch(date, restaurant, requestedDishes));
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void update(Long restaurantId, LocalDate date, LinkedHashSet<Long> dishIds) {
        log.debug("update in restaurant {} on {} to new dishes{}", restaurantId, date, dishIds);

        Lunch lunch = lunchRepository.getFromRestaurantOnDate(restaurantId, date);
        checkLunchExists(lunch, restaurantId, date);

        checkDeadline(date, "modify lunch");

        List<Dish> requestedDishes = getRequestedDishes(restaurantId, dishIds);
        lunch.setDishes(requestedDishes);
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void enable(Long restaurantId, LocalDate date, boolean enabled) {
        log.debug(enabled ? "enable lunch in {} on {}" : "disable lunch in restaurant {} on {}", restaurantId, date);
        Lunch lunch = lunchRepository.getFromRestaurantOnDate(restaurantId, date);
        checkLunchExists(lunch, restaurantId, date);
        lunch.setEnabled(enabled);
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void delete(Long restaurantId, LocalDate date) {
        log.debug("delete lunch from {} on {}", restaurantId, date);
        Lunch lunch = lunchRepository.getFromRestaurantOnDate(restaurantId, date);
        checkLunchExists(lunch, restaurantId, date);
        lunchRepository.delete(lunch);
    }

    /*
     * Retrieves dishes from restaurant by their IDs.
     * Checks if dishes are valid (number of retrieved dishes must match number of requested dishes).
     * Checks if dishes are available (it is forbidden to add to lunch unavailable dishes).
     */
    private List<Dish> getRequestedDishes(Long restaurantId, LinkedHashSet<Long> dishIds) {
        Map<Long, Dish> dishes = dishRepository.getByIds(restaurantId, dishIds).stream()
                .collect(toMap(Dish::getId, d -> d));

        int numberOfDishesToAdd = dishIds.size();
        if (dishes.size() != numberOfDishesToAdd) {
            throw new IllegalArgumentException("invalid dishes");
        }

        List<Dish> dishesOrderedAsRequested = dishIds.stream()
                .map(dishes::get)
                .filter(Dish::isEnabled)
                .toList();
        if (dishesOrderedAsRequested.size() != numberOfDishesToAdd) {
            throw new IllegalArgumentException("you can't add unavailable dishes to lunch");
        }

        return dishesOrderedAsRequested;
    }

}
