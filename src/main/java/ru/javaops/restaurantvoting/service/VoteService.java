package ru.javaops.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.error.InvalidVoteException;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.model.Vote;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class VoteService {

    private static final LocalTime REVOTE_DEADLINE = LocalTime.of(11, 0);

    private VoteRepository voteRepository;

    private LunchRepository lunchRepository;

    @Transactional
    public void vote(LocalDate date, User user, int restaurantId) {
        LunchAndVote lunchAndVote = lunchRepository.getLunchAndVotePair(date, user.getId(), restaurantId);
        Lunch lunch = lunchAndVote.lunch();
        if (lunch == null) {
            throw new InvalidVoteException("no lunch from this restaurant on " + date);
        }

        Vote vote = lunchAndVote.vote();
        if (vote != null) {
            if (LocalDateTime.now().isAfter(LocalDateTime.of(date, REVOTE_DEADLINE))) {
                throw new InvalidVoteException("you can't revote after 11:00");
            }
            if (vote.getLunch().getId().equals(lunch.getId())) {
                throw new InvalidVoteException("you've already voted for this restaurant");
            }
            vote.setLunch(lunch);
        } else {
            vote = new Vote(date, user, lunch);
        }
        voteRepository.save(vote);
    }
}
