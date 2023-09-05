package ru.javaops.restaurantvoting.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.service.LunchService;
import ru.javaops.restaurantvoting.to.LunchTo;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping(value = LunchController.LUNCH_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class LunchController {

    public static final String LUNCH_URL = "/api/restaurants/{restaurantId}/lunches/{date}";

    private LunchRepository lunchRepository;

    private LunchService lunchService;

    @GetMapping
    public LunchTo get(@PathVariable int restaurantId, @PathVariable LocalDate date) {
        return lunchService.get(restaurantId, date);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public void add(@PathVariable int restaurantId, @PathVariable LocalDate date, @RequestBody Set<Integer> dishIds) {
        lunchService.add(restaurantId, date, dishIds);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public void update(@PathVariable int restaurantId, @PathVariable LocalDate date, @RequestBody Set<Integer> dishIds) {
        lunchService.update(restaurantId, date, dishIds);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    public void delete(@PathVariable int restaurantId, @PathVariable LocalDate date) {
        lunchService.delete(restaurantId, date);
    }
}
