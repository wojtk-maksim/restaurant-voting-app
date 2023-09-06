package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.User;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles ORDER BY u.name, u.email")
    List<User> getAll();

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email=:email AND u.deleted=FALSE")
    Optional<User> getByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name=:name, u.email=:email, u.password=:password WHERE u.id=:id AND u.deleted=FALSE")
    int update(int id, String name, String email, String password);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.enabled=:enabled WHERE u.id=:id")
    int enable(int id, boolean enabled);

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN TRUE ELSE FALSE END FROM User u WHERE u.email=LOWER(:email)")
    boolean emailExists(String email);

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN TRUE ELSE FALSE END FROM User u WHERE u.id=:id AND u.password=:password")
    boolean passwordMatches(int id, String password);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.deleted=TRUE WHERE u.id=:id")
    int delete(int id);
}
