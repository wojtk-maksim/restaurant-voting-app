package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Dish;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Long> {

    @Query("FROM Dish d WHERE d.restaurant.id=:restaurantId ORDER BY d.deleted, d.enabled DESC, d.name")
    List<Dish> getAllFromRestaurant(Long restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.id=:id")
    Dish get(Long restaurantId, Long id);

    @Query("SELECT d FROM Dish d WHERE d.id IN :dishIds")
    List<Dish> getDishesForLunch(List<Long> dishIds);

    boolean existsByRestaurantIdAndName(Long restaurantId, String name);

    @Modifying
    @Transactional
    @Query("UPDATE Dish d SET d.name=:name, d.price=:price WHERE d.restaurant.id=:restaurantId AND d.id=:id")
    Integer update(Long restaurantId, Long id, String name, int price);

    @Modifying
    @Transactional
    @Query("UPDATE Dish d SET d.enabled=:enabled WHERE d.restaurant.id=:restaurantId AND d.id=:id")
    Integer enable(Long restaurantId, Long id, boolean enabled);

    @Modifying
    @Transactional
    @Query("UPDATE Dish d SET d.deleted=TRUE WHERE d.restaurant.id=:restaurantId AND d.id=:id")
    Integer softDelete(Long restaurantId, Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.id=:id")
    Integer hardDelete(Long restaurantId, Long id);

}
