package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.to.VoteValidationObject;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface LunchRepository extends JpaRepository<Lunch, Long> {

    @Query("SELECT l FROM Lunch l JOIN FETCH l.dishes WHERE l.restaurant.id=:restaurantId AND l.date=:date")
    Optional<Lunch> getFromRestaurantOnDate(long restaurantId, LocalDate date);

    @Query("FROM Lunch l JOIN FETCH l.restaurant JOIN FETCH l.dishes WHERE l.date=:date")
    List<Lunch> getAllOnDate(LocalDate date);

    @Modifying
    @Transactional
    @Query("DELETE FROM Lunch l WHERE l.restaurant.id=:restaurantId AND l.date=:date")
    int delete(long restaurantId, LocalDate date);

    @Query("""
            SELECT new ru.javaops.restaurantvoting.to.VoteValidationObject(r, l, v, u) FROM Lunch l JOIN FETCH l.dishes
            LEFT JOIN User u ON u.id=:userId
            LEFT JOIN Restaurant r ON l.restaurant=r
            LEFT JOIN Vote v ON v.date=:date AND v.user=u
            WHERE r.id=:restaurantId AND l.date=:date AND l.restaurant=r
            """)
    VoteValidationObject getRestaurantLunchVote(long restaurantId, LocalDate date, long userId);
}
