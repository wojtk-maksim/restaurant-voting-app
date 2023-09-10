package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.BaseEntity;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.to.dish.SimpleDishTo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class DishService {

    RestaurantService restaurantService;

    RestaurantRepository restaurantRepository;

    DishRepository dishRepository;

    @Cacheable(value = "dishes", key = "#restaurantId")
    public Map<Long, Dish> getAllFromRestaurant(long restaurantId) {
        log.debug("get all from restaurant {}", restaurantId);
        return dishRepository.getAllFromRestaurant(restaurantId).stream()
                .collect(Collectors.toMap(BaseEntity::getId, d -> d, (k, v) -> v, LinkedHashMap::new));
    }

    public Dish getFromRestaurant(long restaurantId, long id) {
        log.debug("get {} from restaurant {}", id, restaurantId);
        return dishRepository.get(restaurantId, id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurantId", allEntries = true)
    public Dish add(long restaurantId, SimpleDishTo newDish) {
        log.debug("add new {} to restaurant {}", newDish, restaurantId);
        if (dishRepository.exists(restaurantId, newDish.getName())) {
            throw new IllegalArgumentException("dish with this name already exists");
        }
        return dishRepository.save(
                new Dish(newDish.getName(), newDish.getPrice(), restaurantRepository.getReferenceById(restaurantId))
        );
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurantId", allEntries = true)
    public void update(long restaurantId, long id, SimpleDishTo simpleDishTo) {
        Dish dish = dishRepository.get(restaurantId, id).orElseThrow(NotFoundException::new);
        if (!dish.getName().equals(simpleDishTo.getName()) && dishRepository.exists(restaurantId, simpleDishTo.getName())) {
            throw new IllegalArgumentException("dish with this name already exists");
        }
        dish.setName(simpleDishTo.getName());
        dish.setPrice(simpleDishTo.getPrice());
        dishRepository.save(dish);
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurantId", allEntries = true)
    public void enable(long restaurantId, long id, boolean enabled) {
        log.debug(enabled ? "enable {} in restaurant {}" : "disable {} in restaurant {}", id, restaurantId);
        if (dishRepository.enable(restaurantId, id, enabled) == 0) {
            throw new NotFoundException();
        }
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#restaurantId", allEntries = true)
    public void delete(long restaurantId, long id) {
        log.debug("delete {} from restaurant {}", id, restaurantId);
        if (dishRepository.delete(restaurantId, id) == 0) {
            throw new NotFoundException();
        }
    }

    public List<Dish> getByIds(long restaurantId, Set<Long> dishIds) {
        List<Dish> dishes = dishRepository.getByIds(restaurantId, dishIds);
        if (dishes.isEmpty() || dishes.size() != dishIds.size()) {
            throw new IllegalArgumentException("invalid restaurant/dishes combination");
        }
        return dishes;
    }
}
