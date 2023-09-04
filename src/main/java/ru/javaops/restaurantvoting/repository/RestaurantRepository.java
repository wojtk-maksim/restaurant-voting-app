package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.service.DishService;
import ru.javaops.restaurantvoting.to.NameWithRestaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Restaurant r SET r.name=:name WHERE r.id=:id")
    int update(int id, String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(int id);

    @Query("""
            SELECT new ru.javaops.restaurantvoting.to.NameWithRestaurant(d.name, r) FROM Restaurant r
            LEFT JOIN Dish d ON d.restaurant.id=:restaurantId AND d.name=:name
            WHERE r.id=:restaurantId
            """)
    NameWithRestaurant getNameWithRestaurant(String name, int restaurantId);
}
