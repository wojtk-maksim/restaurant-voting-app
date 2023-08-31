package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<User> getAll() {
        log.info("get all");
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    public User get(int id) {
        log.info("get {}", id);
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public User getByEmail(String email) {
        log.info("get by email '{}'", email);
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public User createOrUpdate(User user) {
        log.info("{} {}", user.getId() == null ? "save" : "update", user);
        return userRepository.save(user);
    }

    @Transactional
    public void delete(int id) {
        log.info("delete with id {}", id);
        userRepository.delete(userRepository.findById(id).orElseThrow(NotFoundException::new));
    }
}
