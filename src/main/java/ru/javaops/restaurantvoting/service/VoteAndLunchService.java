package ru.javaops.restaurantvoting.service;

import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.*;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.repository.VoteRepository;
import ru.javaops.restaurantvoting.to.DishTo;
import ru.javaops.restaurantvoting.to.RestaurantTo;
import ru.javaops.restaurantvoting.to.lunch.CachedLunchObject;
import ru.javaops.restaurantvoting.to.lunch.LunchForVoting;
import ru.javaops.restaurantvoting.to.user.VoterTo;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;
import static ru.javaops.restaurantvoting.util.DateTimeUtil.checkDeadline;
import static ru.javaops.restaurantvoting.util.LunchUtil.checkLunchAvailable;
import static ru.javaops.restaurantvoting.util.LunchUtil.checkLunchFound;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class VoteAndLunchService extends AbstractLunchService {

    private final VoteRepository voteRepository;

    public VoteAndLunchService(CacheHelper cacheHelper, LunchRepository lunchRepository,
                               VoteRepository voteRepository, DishRepository dishRepository) {
        super(cacheHelper, lunchRepository, dishRepository);
        this.voteRepository = voteRepository;
    }

    public List<LunchForVoting> getLunchesForVoting(LocalDate date) {
        log.debug("Get all lunches on with voters on {}", date);
        Map<Long, CachedLunchObject> lunches = cacheHelper.getCachedLunchesOnDate(date);
        if (lunches.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, RestaurantTo> restaurants = cacheHelper.getRestaurantsCached();
        Map<Long, List<VoterTo>> voters = voteRepository.getVotersOnDate(date).stream()
                .collect(groupingBy(VoterTo::getLunchId));
        return lunches.entrySet().stream()
                .map(entry -> {
                    CachedLunchObject lunch = entry.getValue();
                    Long restaurantId = entry.getKey();
                    return lunchForVotingTo(restaurantId, restaurants.get(restaurantId), lunch, voters.get(lunch.getId()));
                })
                .toList();
    }

    @Transactional
    public void vote(LocalDate date, Long userId, Long restaurantId) {
        log.debug("On {} user {} votes for {}", date, userId, restaurantId);
        checkDeadline(date, "vote");

        RestaurantTo restaurant = getRestaurantIfExists(restaurantId);
        checkRestaurantDeleted(restaurant);
        checkRestaurantAvailable(restaurant);

        Tuple voteValidationTuple = voteRepository.getVoteValidationTuple(date, restaurantId, userId);

        Lunch lunch = (Lunch) voteValidationTuple.get("lunch");
        checkLunchFound(lunch, date, restaurant);
        checkLunchAvailable(lunch, date, restaurant);

        Vote vote = (Vote) voteValidationTuple.get("vote");
        if (vote != null) {
            vote.setLunch(lunch);
            // Let Hibernate know that vote is not new with special boolean field 'repeatedVote'.
            vote.setRepeatedVote(true);
        } else {
            vote = new Vote(date, (User) voteValidationTuple.get("user"), lunch);
        }
        voteRepository.save(vote);
    }

    @Transactional
    @CacheEvict(value = "lunches", key = "#date", allEntries = true)
    public void batchCreateOrUpdate(LocalDate date, LinkedHashMap<Long, List<Long>> dishIdsByRestaurantIds,
                                    List<Long> allRestaurantIds, List<Long> allDishIds) {
        log.debug("On {} batch create or update lunches (restaurant ID -> dish IDs): {}", date, dishIdsByRestaurantIds);
        checkDeadline(date, "lunch");

        // Get restaurants and lunches (if they've been already added) from repository.
        // Map each lunch by its restaurant ID.
        Map<Long, Lunch> lunches = lunchRepository.getBatchLunchValidationTuples(date, allRestaurantIds).stream()
                .map(tuple -> {
                    Restaurant restaurant = (Restaurant) tuple.get("restaurant");
                    Lunch lunch = (Lunch) tuple.get("lunch");
                    return lunch != null ? lunch : new Lunch(date, restaurant);
                })
                .collect(toMap(l -> l.getRestaurant().getId(), l -> l));

        // Get all dishes for all lunches. Every dish is mapped by its ID.
        Map<Long, Dish> dishesForLunches = dishRepository.getDishesForLunch(allDishIds).stream()
                .collect(toMap(Dish::getId, d -> d));

        List<Lunch> lunchesForSaving = dishIdsByRestaurantIds.entrySet().stream()
                .map(entry -> {
                    List<Dish> dishesForLunch = entry.getValue().stream()
                            .map(dishesForLunches::get)
                            .collect(toList());
                    Lunch lunch = lunches.get(entry.getKey());
                    lunch.setDishes(dishesForLunch);
                    return lunch;
                })
                .toList();
        lunchRepository.saveAll(lunchesForSaving);
    }

    private LunchForVoting lunchForVotingTo(Long restaurantId, RestaurantTo restaurant,
                                            CachedLunchObject lunch, List<VoterTo> voters) {
        List<DishTo> dishesInLunch = getDishesInLunch(restaurantId, lunch.getDishIds());
        return new LunchForVoting(restaurant, lunch.getId(), dishesInLunch, lunch.isEnabled(), voters);
    }

}
