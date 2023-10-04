package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Restaurant;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT r FROM Restaurant r ORDER BY r.deleted, r.enabled DESC, r.name")
    List<Restaurant> getAll();

    @Query("SELECT r FROM Restaurant r WHERE r.id=:id")
    Restaurant get(Long id);

    boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Restaurant r SET r.name=:name WHERE r.id=:id")
    Integer update(long id, String name);

    @Modifying
    @Transactional
    @Query("UPDATE Restaurant r SET r.enabled=:enabled WHERE r.id=:id")
    Integer enable(long id, boolean enabled);

    @Modifying
    @Transactional
    @Query("UPDATE Restaurant r SET r.deleted=TRUE WHERE r.id=:id")
    Integer softDelete(long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    Integer hardDelete(long id);

}
