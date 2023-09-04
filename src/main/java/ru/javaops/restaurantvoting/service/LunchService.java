package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.to.LunchTo;

import java.time.LocalDate;
import java.util.Set;

import static ru.javaops.restaurantvoting.to.ToConverter.lunchTo;

@Service
@AllArgsConstructor
public class LunchService {

    private RestaurantRepository restaurantRepository;

    private LunchRepository lunchRepository;

    private DishService dishService;

    public LunchTo get(int restaurantId, LocalDate date) {
        return lunchTo(lunchRepository.getWithDishes(restaurantId, date).orElseThrow(NotFoundException::new));
    }

    @Transactional
    public void add(int restaurantId, LocalDate date, Set<Integer> dishIds) {
        if (lunchRepository.exists(restaurantId, date)) {
            throw new IllegalArgumentException("there is lunch on this date already");
        }

        lunchRepository.save(new Lunch(
                date,
                restaurantRepository.getReferenceById(restaurantId),
                dishService.getByIds(restaurantId, dishIds))
        );
    }

    @Transactional
    public void update(int restaurantId, LocalDate date, Set<Integer> dishIds) {
        Lunch lunch = lunchRepository.getWithDishes(restaurantId, date).orElseThrow(NotFoundException::new);

        lunch.setDishes(dishService.getByIds(restaurantId, dishIds));
        lunchRepository.save(lunch);
    }

    @Transactional
    public void delete(int restaurantId, LocalDate date) {
        if (lunchRepository.delete(restaurantId, date) != 1) {
            throw new NotFoundException();
        }
    }
}
