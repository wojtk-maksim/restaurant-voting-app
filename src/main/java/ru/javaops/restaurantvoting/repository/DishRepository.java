package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.to.RestaurantDishTo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Query("SELECT new ru.javaops.restaurantvoting.to.RestaurantDishTo(d.id, d.name, d.price) FROM Dish d WHERE d.restaurant.id=:restaurantId")
    List<RestaurantDishTo> getAllFromRestaurant(int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.id=:id")
    Optional<Dish> get(int restaurantId, int id);

    @Query("SELECT new ru.javaops.restaurantvoting.to.RestaurantDishTo(d.id, d.name, d.price) FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.id=:id")
    Optional<RestaurantDishTo> getRestaurantDishTo(int restaurantId, int id);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.id IN :ids")
    Set<Dish> getByIds(int restaurantId, Set<Integer> ids);

    @Modifying
    @Transactional
    @Query("DELETE FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.id=:id")
    int delete(int restaurantId, int id);

    @Query("SELECT CASE WHEN (COUNT(d) > 0) THEN TRUE ELSE FALSE END FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.name=:name")
    boolean exists(int restaurantId, String name);
}
