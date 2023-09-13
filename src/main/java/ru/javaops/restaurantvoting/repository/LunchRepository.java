package ru.javaops.restaurantvoting.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Lunch;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface LunchRepository extends JpaRepository<Lunch, Long> {

    @Query("SELECT l FROM Lunch l JOIN FETCH l.restaurant JOIN FETCH l.dishes WHERE l.restaurant.id=:restaurantId AND l.date=:date")
    Lunch getFromRestaurantOnDate(Long restaurantId, LocalDate date);

    @Query("SELECT l FROM Lunch l JOIN FETCH l.restaurant JOIN FETCH l.dishes WHERE l.date=:date")
    List<Lunch> getAllOnDate(LocalDate date);

    @Modifying
    @Transactional
    @Query("DELETE FROM Lunch l WHERE l.restaurant.id=:restaurantId AND l.date=:date")
    int delete(Long restaurantId, LocalDate date);

    @Query("""
            SELECT l AS lunch, v AS vote, u AS user FROM Lunch l JOIN FETCH l.dishes
            LEFT JOIN User u ON u.id=:userId
            LEFT JOIN Restaurant r ON l.restaurant=r
            LEFT JOIN Vote v ON v.date=:date AND v.user=u
            WHERE r.id=:restaurantId AND l.date=:date AND l.restaurant=r
            """)
    Tuple getVoteValidationTuple(Long restaurantId, LocalDate date, Long userId);

}
