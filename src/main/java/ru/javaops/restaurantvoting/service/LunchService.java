package ru.javaops.restaurantvoting.service;

import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.to.DishTo;
import ru.javaops.restaurantvoting.to.RestaurantItemPair;
import ru.javaops.restaurantvoting.to.RestaurantTo;
import ru.javaops.restaurantvoting.to.lunch.CachedLunchObject;
import ru.javaops.restaurantvoting.to.lunch.LunchTo;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.restaurantvoting.util.DateTimeUtil.checkDeadline;
import static ru.javaops.restaurantvoting.util.LunchUtil.checkLunchFound;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.checkRestaurantFound;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.getRestaurantIfExists;

@Service
@Transactional(readOnly = true)
@Slf4j
public class LunchService extends AbstractLunchService {

    public LunchService(CacheHelper cacheHelper, DishRepository dishRepository, LunchRepository lunchRepository) {
        super(cacheHelper, lunchRepository, dishRepository);
    }

    // CachedLunchObject keeps IDS of dishes. We need to "build" lunch by retrieving dishes from cache.
    public RestaurantItemPair<LunchTo> getFromRestaurantOnDate(Long restaurantId, LocalDate date) {
        RestaurantTo restaurant = getRestaurantIfExists(restaurantId);
        log.debug("Get lunch from restaurant {} on {}", restaurant, date);
        CachedLunchObject cachedLunch = cacheHelper.getCachedLunchesOnDate(date).get(restaurantId);
        checkLunchFound(cachedLunch, date, restaurant);
        List<DishTo> dishesInLunch = getDishesInLunch(restaurantId, cachedLunch.getDishIds());
        LunchTo lunch = new LunchTo(cachedLunch.getId(), dishesInLunch, cachedLunch.isEnabled());
        return new RestaurantItemPair<>(restaurant, lunch);
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void createOrUpdate(Long restaurantId, LocalDate date, List<Long> dishIds) {
        // Not checking if restaurant exists and if dishes exist/not deleted/available.
        // Rely on DishesForLunchValidator.
        checkDeadline(date, "lunch");
        Tuple lunchValidationTuple = lunchRepository.getLunchValidationTuple(restaurantId, date);
        Restaurant restaurant = (Restaurant) lunchValidationTuple.get("restaurant");
        log.debug("Add lunch to restaurant {} on {} with dishes {}", restaurant, date, dishIds);
        checkRestaurantFound(restaurant, restaurantId);
        List<Dish> dishesForLunch = dishRepository.getDishesForLunch(dishIds);
        Lunch lunch = (Lunch) lunchValidationTuple.get("lunch");
        if (lunch == null) {
            lunch = new Lunch(date, restaurant, dishesForLunch);
        } else {
            lunch.setDishes(dishesForLunch);
        }
        lunchRepository.save(lunch);
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void enable(Long restaurantId, LocalDate date, boolean enabled) {
        RestaurantTo restaurant = getRestaurantIfExists(restaurantId);
        log.debug(enabled ? "Enable lunch in restaurant {} on {}" : "Disable lunch in restaurant {} on {}", restaurantId, date);
        checkLunchFound(lunchRepository.enable(restaurantId, date, enabled), date, restaurant);
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void delete(Long restaurantId, LocalDate date) {
        RestaurantTo restaurant = getRestaurantIfExists(restaurantId);
        log.debug("Delete lunch from restaurant {} on {}", restaurantId, date);
        checkLunchFound(lunchRepository.delete(restaurantId, date), date, restaurant);
    }

}
