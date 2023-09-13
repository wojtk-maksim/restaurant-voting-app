package ru.javaops.restaurantvoting.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT r FROM Restaurant r ORDER BY r.deleted, r.enabled DESC, r.name")
    List<Restaurant> getAll();

    @Query("SELECT r FROM Restaurant r WHERE r.id=:id")
    Restaurant get(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Restaurant r SET r.name=:name WHERE r.id=:id")
    int update(long id, String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(long id);

    @Modifying
    @Transactional
    @Query("UPDATE Restaurant r SET r.enabled=:enabled WHERE r.id=:id")
    int enable(long id, boolean enabled);

    @Query("""
            SELECT r AS restaurant, l AS lunch FROM Restaurant r
            LEFT JOIN Lunch l ON l.date=:date AND l.restaurant=r
            WHERE r.id=:id
            """)
    Tuple getLunchValidationTuple(long id, LocalDate date);

}
