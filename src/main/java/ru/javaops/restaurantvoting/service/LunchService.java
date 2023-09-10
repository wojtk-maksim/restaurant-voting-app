package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.to.RestaurantAndLunch;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class LunchService {

    private RestaurantService restaurantService;

    private RestaurantRepository restaurantRepository;

    private LunchRepository lunchRepository;

    private DishRepository dishRepository;


    public Lunch getFromRestaurantOnDate(long restaurantId, LocalDate date) {
        return lunchRepository.getFromRestaurantOnDate(restaurantId, date).orElseThrow(NotFoundException::new);
    }

    @Cacheable("lunches")
    public List<Lunch> getAllOnDate(LocalDate date) {
        log.debug("get all on {}", date);
        return lunchRepository.getAllOnDate(date);
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void add(long restaurantId, LocalDate date, Set<Long> dishIds) {
        log.debug("add to restaurant {} on {} with dishes {}", restaurantId, date, dishIds);
        RestaurantAndLunch restaurantAndLunch = restaurantRepository.getRestaurantAndLunchPair(restaurantId, date);
        if (restaurantAndLunch.lunch() != null) {
            throw new IllegalArgumentException("restaurant " + restaurantId + " already has lunch on " + date);
        }
        List<Dish> dishes = getDishesByRestaurantAndIds(restaurantId, dishIds);
        lunchRepository.save(new Lunch(date, restaurantAndLunch.restaurant(), dishes));
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void update(long restaurantId, LocalDate date, Set<Long> dishIds) {
        log.debug("update in restaurant {} on {} to new dishes{}", restaurantId, date, dishIds);
        Lunch lunch = lunchRepository.getFromRestaurantOnDate(restaurantId, date).orElseThrow(NotFoundException::new);
        List<Dish> dishes = getDishesByRestaurantAndIds(restaurantId, dishIds);
        lunch.setDishes(dishes);
        lunchRepository.save(lunch);
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void delete(long restaurantId, LocalDate date) {
        if (lunchRepository.delete(restaurantId, date) == 0) {
            throw new NotFoundException();
        }
    }

    private List<Dish> getDishesByRestaurantAndIds(long restaurantId, Set<Long> dishIds) {
        List<Dish> dishes = dishRepository.getByRestaurantAndIds(restaurantId, dishIds);
        if (dishes.size() != dishIds.size()) {
            throw new IllegalArgumentException("invalid dishes");
        }
        return dishes;
    }

}
