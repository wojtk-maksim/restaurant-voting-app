package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.error.InvalidVoteException;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.model.Vote;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.repository.VoteRepository;
import ru.javaops.restaurantvoting.to.VoteValidationObject;
import ru.javaops.restaurantvoting.to.VoterTo;
import ru.javaops.restaurantvoting.to.lunch.LunchWithVotersTo;

import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.javaops.restaurantvoting.util.validation.ValidationUtil.checkAvailable;
import static ru.javaops.restaurantvoting.util.validation.ValidationUtil.checkExists;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class VoteService {

    private static final LocalTime REVOTE_DEADLINE = LocalTime.of(11, 0);

    private UserRepository userRepository;

    private VoteRepository voteRepository;

    private LunchRepository lunchRepository;

    private LunchService lunchService;

    public List<LunchWithVotersTo> getOffersOnDate(LocalDate date) {
        List<Lunch> lunches = lunchService.getAllOnDate(date);
        if (lunches.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, List<VoterTo>> votersByLunch = userRepository.getVotersOnDate(date).stream()
                .collect(Collectors.groupingBy(VoterTo::lunchId));
        return lunches.stream()
                .map(l -> new LunchWithVotersTo(l.getRestaurant(), votersByLunch.get(l.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public void vote(LocalDate date, User user, long restaurantId) {
        VoteValidationObject voteValidationObject = lunchRepository.getRestaurantLunchVote(restaurantId, date, user.getId());

        Restaurant restaurant = voteValidationObject.restaurant();
        checkAvailable(checkExists(restaurant));

        Lunch lunch = voteValidationObject.lunch();
        checkExists(lunch);

        Vote vote = voteValidationObject.vote();
        if (vote != null) {
            if (lunch.getId().equals(vote.getLunch().getId())) {
                throw new InvalidVoteException("you have already voted for this lunch");
            }
            if (currentDateTime().isAfter(deadline(date))) {
                throw new InvalidVoteException("you can't revote after 11:00");
            }
            vote.setLunch(lunch);
            return;
        }

        voteRepository.save(new Vote(null, date, voteValidationObject.user(), lunch));
    }

    private static LocalDateTime currentDateTime() {
        ZonedDateTime currentDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Europe/Moscow"));
        return LocalDateTime.of(currentDateTime.toLocalDate(), currentDateTime.toLocalTime());
    }

    private static LocalDateTime deadline(LocalDate date) {
        return LocalDateTime.of(date, REVOTE_DEADLINE);
    }

}
