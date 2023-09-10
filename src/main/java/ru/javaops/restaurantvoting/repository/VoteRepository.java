package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Vote;

import java.time.LocalDate;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("""
            SELECT CASE WHEN v IS NOT NULL THEN TRUE ELSE FALSE END FROM Vote v
            JOIN Lunch l ON l=v.lunch
            WHERE v.date=:date AND v.user.id=:userId AND l.restaurant.id=:restaurantId
            """)
    boolean checkVote(LocalDate date, long userId, long restaurantId);

}
