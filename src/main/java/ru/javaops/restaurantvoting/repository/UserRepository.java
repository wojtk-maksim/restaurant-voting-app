package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.to.VoterTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.id=:id")
    User get(int id);

    @Query("SELECT u FROM User u JOIN FETCH u.roles ORDER BY u.name, u.email")
    List<User> getAll();

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email=:email AND u.deleted=FALSE")
    Optional<User> getByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name=:name, u.email=:email, u.password=:password WHERE u.id=:id AND u.deleted=FALSE")
    int update(long id, String name, String email, String password);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.enabled=:enabled WHERE u.id=:id")
    int enable(long id, boolean enabled);

    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.deleted=TRUE WHERE u.id=:id")
    int delete(long id);

    @Query("""
            SELECT new ru.javaops.restaurantvoting.to.VoterTo(u.id, u.name, v.lunch.id) FROM User u
            JOIN Vote v ON u=v.user WHERE v.date=:date
            """)
    List<VoterTo> getVotersOnDate(LocalDate date);
}
