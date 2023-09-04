package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.service.LunchAndVote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface LunchRepository extends JpaRepository<Lunch, Integer> {

    @Query("""
            SELECT new ru.javaops.restaurantvoting.service.LunchAndVote(l, v) FROM Lunch l
            LEFT JOIN Vote v ON v.date=:date AND v.user.id=:userId
            WHERE l.date=:date AND l.restaurant.id=:restaurantId
            """)
    LunchAndVote getLunchAndVotePair(LocalDate date, int userId, int restaurantId);


    @Query("SELECT l FROM Lunch l JOIN FETCH l.dishes WHERE l.restaurant.id=:restaurantId AND l.date=:date")
    Optional<Lunch> getWithDishes(int restaurantId, LocalDate date);

    @Query("SELECT l FROM Lunch l JOIN FETCH l.dishes WHERE l.date=:date")
    List<Lunch> getAllOnDate(LocalDate date);

    @Query("SELECT CASE WHEN (COUNT(l) > 0) THEN TRUE ELSE FALSE END FROM Lunch l WHERE l.restaurant.id=:restaurantId AND l.date=:date")
    boolean exists(int restaurantId, LocalDate date);

    @Modifying
    @Transactional
    @Query("DELETE FROM Lunch l WHERE l.restaurant.id=:restaurantId AND l.date=:date")
    int delete(int restaurantId, LocalDate date);
}
