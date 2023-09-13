package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Vote;

import java.time.LocalDate;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Long> {

    // Used in tests to assure vote was saved
    @Query("""
            SELECT CASE WHEN EXISTS (
                SELECT v
                FROM Vote v
                JOIN Lunch l ON l=v.lunch
                WHERE v.date=:date AND v.user.id=:userId AND l.restaurant.id=:restaurantId
            )
            THEN TRUE
            ELSE FALSE END
            """)
    boolean exists(LocalDate date, long userId, long restaurantId);

}
