package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.to.lunch.VoterTo;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.id=:id")
    User get(long id);

    @Query("SELECT u FROM User u ORDER BY u.name, u.email")
    List<User> getAll();

    @Query("SELECT u FROM User u WHERE u.email=:email")
    User getByEmail(String email);

    @Query("SELECT u.email FROM User u WHERE u.email=:email")
    String getEmail(String email);

    @Query("""
            SELECT new ru.javaops.restaurantvoting.to.lunch.VoterTo(u.id, u.name, v.lunch.id) FROM User u
            JOIN Vote v ON u=v.user
            WHERE v.date=:date
            """)
    List<VoterTo> getVotersOnDate(LocalDate date);

    boolean existsByEmail(String email);

}
