package ru.javaops.restaurantvoting.service;

import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.model.Vote;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.repository.VoteRepository;
import ru.javaops.restaurantvoting.to.lunch.LunchWithVotersTo;
import ru.javaops.restaurantvoting.to.lunch.VoterTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static ru.javaops.restaurantvoting.util.LunchUtil.checkLunchAvailableForVoting;
import static ru.javaops.restaurantvoting.util.LunchUtil.checkLunchExists;
import static ru.javaops.restaurantvoting.util.validation.ValidationUtil.checkDeadline;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class VoteService {

    private UserRepository userRepository;

    private VoteRepository voteRepository;

    private LunchRepository lunchRepository;

    private LunchService lunchService;

    public List<LunchWithVotersTo> getOffersOnDate(LocalDate date) {
        List<Lunch> lunches = lunchService.getAllOnDate(date).stream()
                .filter(l -> l.isEnabled() && l.getRestaurant().isEnabled() && !l.getRestaurant().isDeleted())
                .toList();
        if (lunches.isEmpty()) {
            return emptyList();
        }
        Map<Long, List<VoterTo>> votersByLunch = userRepository.getVotersOnDate(date).stream()
                .collect(groupingBy(VoterTo::restaurantId));
        return lunches.stream()
                .map(l -> new LunchWithVotersTo(
                        l,
                        votersByLunch.get(l.getRestaurant().getId())
                ))
                .toList();
    }

    @Transactional
    public void vote(LocalDate date, Long userId, Long restaurantId) {
        Tuple voteValidationTuple = lunchRepository.getVoteValidationTuple(restaurantId, date, userId);

        Lunch lunch = (Lunch) voteValidationTuple.get("lunch");
        checkLunchExists(lunch, restaurantId, date);
        checkLunchAvailableForVoting(lunch, restaurantId, date);

        Vote vote = (Vote) voteValidationTuple.get("vote");
        if (vote != null) {
            if (lunch.equals(vote.getLunch())) {
                throw new IllegalArgumentException("you have already voted for this restaurant");
            }
            checkDeadline(date, "vote for another restaurant");
            vote.setLunch(lunch);
        } else {
            User user = (User) voteValidationTuple.get("user");
            voteRepository.save(new Vote(null, date, user, lunch));
        }
    }

}
