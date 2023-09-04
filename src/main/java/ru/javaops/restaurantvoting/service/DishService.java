package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.to.NameWithRestaurant;
import ru.javaops.restaurantvoting.to.RestaurantDishTo;
import ru.javaops.restaurantvoting.to.SimpleDishTo;

import java.util.Set;

import static ru.javaops.restaurantvoting.to.ToConverter.restaurantDishTo;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DishService {

    RestaurantRepository restaurantRepository;

    DishRepository dishRepository;

    @Transactional
    public RestaurantDishTo add(SimpleDishTo dish, int restaurantId) {
        NameWithRestaurant nameWithRestaurant = restaurantRepository.getNameWithRestaurant(dish.name(), restaurantId);
        if (nameWithRestaurant.restaurant() == null) {
            throw new IllegalArgumentException("restaurant does not exist");
        }
        if (nameWithRestaurant.name() != null) {
            throw new IllegalArgumentException("dish already exists");
        }
        return restaurantDishTo(dishRepository.save(new Dish(dish.name(), dish.price(), nameWithRestaurant.restaurant())));
    }

    @Transactional
    public void update(int restaurantId, int id, SimpleDishTo simpleDishTo) {
        Dish dish = dishRepository.get(restaurantId, id).orElseThrow(NotFoundException::new);
        if (dishRepository.exists(restaurantId, simpleDishTo.name())) {
            throw new IllegalArgumentException("dish with this name already exists");
        }
        dish.setName(simpleDishTo.name());
        dish.setPrice(simpleDishTo.price());
        dishRepository.save(dish);
    }

    public Set<Dish> getByIds(int restaurantId, Set<Integer> dishIds) {
        Set<Dish> dishes = dishRepository.getByIds(restaurantId, dishIds);
        if (dishes.isEmpty() || dishes.size() != dishIds.size()) {
            throw new IllegalArgumentException("invalid restaurant/dishes combination");
        }
        return dishes;
    }
}
