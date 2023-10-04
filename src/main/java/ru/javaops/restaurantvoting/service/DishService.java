package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.to.DishTo;
import ru.javaops.restaurantvoting.to.RestaurantItemPair;
import ru.javaops.restaurantvoting.to.RestaurantTo;

import java.util.ArrayList;
import java.util.List;

import static ru.javaops.restaurantvoting.util.DishUtil.checkDishFound;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.getRestaurantIfExists;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class DishService {

    private final RestaurantRepository restaurantRepository;

    private final DishRepository dishRepository;

    private final CacheHelper cacheHelper;

    public RestaurantItemPair<List<DishTo>> getAllFromRestaurant(Long restaurantId) {
        RestaurantTo restaurant = getRestaurantIfExists(restaurantId);
        log.debug("Get all dishes from restaurant {}", restaurant);
        ArrayList<DishTo> dishes = new ArrayList<>(cacheHelper.getAllDishesCachedFromRestaurant(restaurantId).values());
        return new RestaurantItemPair<>(restaurant, dishes);
    }

    public RestaurantItemPair<DishTo> getFromRestaurant(Long restaurantId, Long id) {
        RestaurantTo restaurant = getRestaurantIfExists(restaurantId);
        log.debug("Get dish {} from restaurant {}", id, restaurant);
        DishTo dish = cacheHelper.getAllDishesCachedFromRestaurant(restaurantId).get(id);
        checkDishFound(dish, id, restaurant);
        return new RestaurantItemPair<>(restaurant, dish);
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurantId", allEntries = true)
    public DishTo add(Long restaurantId, String name, int price) {
        // Not checking if restaurant not found or if dish with this name already exists.
        // Rely on DishUniqueNameValidator.
        log.debug("Add new dish [name = '{}', price = '{}'] to restaurant {}", name, price, restaurantId);
        return dishTo(dishRepository.save(
                new Dish(name, price, restaurantRepository.getReferenceById(restaurantId))
        ));
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurantId", allEntries = true)
    public void update(Long restaurantId, Long id, String name, int price) {
        // Not checking if restaurant or dish not found or if dish with this name already exists.
        // Rely on DishUniqueNameValidator.
        log.debug("Update dish {} to name = {}, price = {} in restaurant {}", id, name, price, restaurantId);
        dishRepository.update(restaurantId, id, name, price);
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurantId", allEntries = true)
    public void enable(Long restaurantId, Long id, boolean enabled) {
        RestaurantTo restaurant = getRestaurantIfExists(restaurantId);
        log.debug(enabled ? "Enable dish {} in restaurant {}" : "Disable dish {} in restaurant {}", id, restaurant);
        checkDishFound(dishRepository.enable(restaurantId, id, enabled), id, restaurant);
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurantId", allEntries = true)
    public void softDelete(Long restaurantId, Long id) {
        RestaurantTo restaurant = getRestaurantIfExists(restaurantId);
        log.debug("Soft delete dish {} from restaurant {}", id, restaurant);
        checkDishFound(dishRepository.softDelete(restaurantId, id), id, restaurant);
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurantId", allEntries = true)
    public void hardDelete(Long restaurantId, Long id) {
        RestaurantTo restaurant = getRestaurantIfExists(restaurantId);
        log.debug("Hard delete dish {} from restaurant {}", id, restaurant);
        checkDishFound(dishRepository.hardDelete(restaurantId, id), id, restaurant);
    }

    public static DishTo dishTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice(), dish.isEnabled(), dish.isDeleted());
    }

}
