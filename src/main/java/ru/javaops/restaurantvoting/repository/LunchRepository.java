package ru.javaops.restaurantvoting.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Lunch;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface LunchRepository extends JpaRepository<Lunch, Long> {

    @Query("FROM Lunch l WHERE l.date=:date AND l.restaurant.id=:restaurantId")
    Lunch getFromRestaurantOnDate(Long restaurantId, LocalDate date);

    @Query("SELECT l FROM Lunch l WHERE l.date=:date ORDER BY l.restaurant.deleted, l.restaurant.enabled DESC, l.enabled DESC")
    List<Lunch> getAllOnDateForCache(LocalDate date);

    @Query("""
            SELECT r AS restaurant, l AS lunch FROM Restaurant r LEFT JOIN Lunch l ON l.date=:date AND l.restaurant=r
            WHERE r.id=:restaurantId
            """)
    Tuple getLunchValidationTuple(Long restaurantId, LocalDate date);

    @Query("""
            SELECT r AS restaurant, l AS lunch FROM Restaurant r
            LEFT JOIN Lunch l ON l.date=:date AND l.restaurant=r WHERE r.id IN :restaurantIds
            """)
    List<Tuple> getBatchLunchValidationTuples(LocalDate date, List<Long> restaurantIds);

    @Modifying
    @Transactional
    @Query("UPDATE Lunch l SET l.enabled=:enabled WHERE l.date=:date AND l.restaurant.id=:restaurantId")
    Integer enable(Long restaurantId, LocalDate date, boolean enabled);

    @Modifying
    @Transactional
    @Query("UPDATE Lunch l SET l.dishes=:dishes WHERE l.date=:date AND l.restaurant.id=:restaurantId")
    Integer update(Long restaurantId, LocalDate date, List<Dish> dishes);

    @Modifying
    @Transactional
    @Query("DELETE FROM Lunch l WHERE l.date=:date AND l.restaurant.id=:restaurantId")
    Integer delete(Long restaurantId, LocalDate date);

}
