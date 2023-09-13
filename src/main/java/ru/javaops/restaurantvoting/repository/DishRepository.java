package ru.javaops.restaurantvoting.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Dish;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Long> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId ORDER BY d.deleted, d.enabled DESC, d.name")
    List<Dish> getAllFromRestaurant(long restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.id=:id")
    Dish get(long restaurantId, long id);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.id IN :ids")
    List<Dish> getByIds(long restaurantId, Set<Long> ids);

    @Modifying
    @Transactional
    @Query("DELETE FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.id=:id")
    int delete(long restaurantId, long id);

    @Query("SELECT CASE WHEN (COUNT(d) > 0) THEN TRUE ELSE FALSE END FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.name=:name")
    boolean exists(long restaurantId, String name);

    @Modifying
    @Transactional
    @Query("UPDATE Dish d SET d.enabled=:enabled WHERE d.restaurant.id=:restaurantId AND d.id=:id")
    int enable(long restaurantId, long id, boolean enabled);

    @Query("SELECT d AS dish, dishWithSameName.name AS name FROM Dish d LEFT JOIN Dish dishWithSameName ON dishWithSameName.name=:name WHERE d.id=:id")
    Tuple getUpdatedDishValidationData(Long id, String name);

    @Query("""
            SELECT r AS restaurant, dishWithSameName.name AS name FROM Restaurant r
            LEFT JOIN Dish dishWithSameName ON dishWithSameName.restaurant=r AND dishWithSameName.name=:name
            WHERE r.id=:restaurantId
            """)
    Tuple getNewDishValidationData(Long restaurantId, String name);

}
