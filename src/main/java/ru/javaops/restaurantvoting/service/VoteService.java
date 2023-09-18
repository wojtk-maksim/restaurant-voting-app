package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.model.Vote;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.repository.VoteRepository;
import ru.javaops.restaurantvoting.to.lunch.LunchWithVoters;
import ru.javaops.restaurantvoting.to.lunch.VoterTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static ru.javaops.restaurantvoting.util.ValidationUtil.*;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class VoteService {

    private UserRepository userRepository;

    private VoteRepository voteRepository;

    private RestaurantService restaurantService;

    private LunchService lunchService;

    public List<LunchWithVoters> getOffersOnDate(LocalDate date) {
        Map<Long, Restaurant> restaurants = restaurantService.getAll();
        Map<Long, List<VoterTo>> voters = userRepository.getVotersOnDate(date).stream()
                .collect(groupingBy(VoterTo::lunchId));

        return lunchService.getAllOnDate(date).entrySet().stream()
                .filter(entry -> {
                    Lunch lunch = entry.getValue();
                    return lunch.isEnabled() &&
                            lunch.getRestaurant().isEnabled()
                            && !lunch.getRestaurant().isDeleted();
                })
                .map(entry -> new LunchWithVoters(
                        restaurants.get(entry.getKey()),
                        entry.getValue().getDishes(),
                        voters.get(entry.getKey())
                ))
                .toList();
    }

    @Transactional
    public void vote(LocalDate date, User user, Restaurant restaurant) {
        log.debug("user {} votes for {} on {}", user, restaurant, date);

        checkDeleted(restaurant);
        checkAvailable(restaurant);

        checkDeadline(date, "vote");

        Lunch lunch = lunchService.getAllOnDate(date).get(restaurant.getId());
        checkLunchExists(lunch, restaurant, date);
        checkLunchAvailable(lunch, restaurant, date);

        voteRepository.save(new Vote(date, user, lunch));
    }

}
