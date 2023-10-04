package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.User;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.id=:id")
    User get(long id);

    @Query("FROM User u ORDER BY u.name, u.email")
    List<User> getAll();

    @Query("SELECT u FROM User u WHERE u.email=:email")
    User getByEmail(String email);

    boolean existsByName(String name);

    boolean existsByEmailIgnoreCase(String email);

    @Query("SELECT u.password FROM User u WHERE u.id=:id")
    String getPassword(Long id);

    @Query("SELECT u.id FROM User u WHERE u.enabled=FALSE")
    Set<Long> getAllBanned();

    @Query("SELECT u.id FROM User u WHERE u.deleted=TRUE")
    Set<Long> getAllDeleted();

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name=:name, u.email=:email, u.password=:password WHERE u.id=:id")
    Integer update(Long id, String name, String email, String password);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name=:name, u.email=:email WHERE u.id=:id")
    Integer updateWithoutPassword(Long id, String name, String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.enabled=:enabled WHERE u.id=:id")
    Integer enable(Long id, boolean enabled);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.deleted=TRUE WHERE u.id=:id")
    Integer softDelete(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id=:id")
    Integer hardDelete(Long id);

}
