package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.to.RestaurantTo;
import ru.javaops.restaurantvoting.util.RestaurantUtil;

import java.util.ArrayList;
import java.util.List;

import static ru.javaops.restaurantvoting.util.RestaurantUtil.checkRestaurantFound;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final CacheHelper cacheHelper;

    public List<RestaurantTo> getAll() {
        log.debug("Get all restaurants");
        return new ArrayList<>(cacheHelper.getRestaurantsCached().values());
    }

    public RestaurantTo get(Long id) {
        log.debug("Get restaurant {}", id);
        return RestaurantUtil.getRestaurantIfExists(id);
    }

    @Transactional
    @CacheEvict(value = {"restaurants", "lunches"}, allEntries = true)
    public RestaurantTo add(String name) {
        // Not checking if name already exists, rely on RestaurantUniqueNameValidator.
        log.debug("Add new restaurant {}", name);
        return restaurantTo(restaurantRepository.save(new Restaurant(name)));
    }

    @Transactional
    @CacheEvict(value = {"restaurants", "lunches"}, allEntries = true)
    public void update(Long id, String name) {
        // Not checking if restaurant found or name already exists, rely on RestaurantUniqueNameValidator.
        log.debug("Update restaurant {} to [name = '{}']", id, name);
        restaurantRepository.update(id, name);
    }

    @Transactional
    @CacheEvict(value = {"restaurants", "lunches"}, allEntries = true)
    public void enable(Long id, boolean enabled) {
        log.debug(enabled ? "Enable restaurant {}" : "Disable restaurant {}", id);
        checkRestaurantFound(restaurantRepository.enable(id, enabled), id);
    }

    @Transactional
    @CacheEvict(value = {"restaurants", "lunches"}, allEntries = true)
    public void softDelete(Long id) {
        log.debug("Soft delete restaurant {}", id);
        checkRestaurantFound(restaurantRepository.softDelete(id), id);
    }

    @Transactional
    @CacheEvict(value = {"restaurants", "lunches"}, allEntries = true)
    public void hardDelete(Long id) {
        log.debug("Hard delete restaurant {}", id);
        checkRestaurantFound(restaurantRepository.hardDelete(id), id);
    }

    public static RestaurantTo restaurantTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.isEnabled(), restaurant.isDeleted());
    }

}
