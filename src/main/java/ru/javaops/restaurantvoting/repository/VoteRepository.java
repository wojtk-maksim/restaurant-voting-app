package ru.javaops.restaurantvoting.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Vote;
import ru.javaops.restaurantvoting.to.user.VoterTo;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(v) > 0 THEN TRUE ELSE FALSE END
            FROM Vote v WHERE v.date=:date AND v.user.id=:userId AND v.lunch.restaurant.id=:restaurantId
            """)
    boolean exists(LocalDate date, Long userId, Long restaurantId);

    @Query("""
            SELECT u AS user, l AS lunch, v AS vote FROM User u
            LEFT JOIN Lunch l ON l.date=:date AND l.restaurant.id=:restaurantId
            LEFT JOIN Vote v ON v.date=:date AND v.user=u
            WHERE u.id=:userId
            """)
    Tuple getVoteValidationTuple(LocalDate date, Long restaurantId, Long userId);

    @Query("""
            SELECT new ru.javaops.restaurantvoting.to.user.VoterTo
                (u.id, u.name, u.role, u.enabled, u.deleted, v.lunch.id)
            FROM Vote v JOIN User u ON u=v.user
            WHERE v.date=:date
            """)
    List<VoterTo> getVotersOnDate(LocalDate date);

}
