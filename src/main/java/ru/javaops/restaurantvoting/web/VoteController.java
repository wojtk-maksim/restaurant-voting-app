package ru.javaops.restaurantvoting.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.service.VoteService;
import ru.javaops.restaurantvoting.to.lunch.LunchWithVotersTo;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.restaurantvoting.web.UrlData.API;
import static ru.javaops.restaurantvoting.web.UrlData.VOTING;

@RestController
@RequestMapping(value = VoteController.VOTING_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {

    public static final String VOTING_URL = API + VOTING;

    private VoteService voteService;

    @GetMapping("/{date}")
    public List<LunchWithVotersTo> getOffersOnDate(@PathVariable LocalDate date) {
        log.info("get all on date {}", date);
        return voteService.getOffersOnDate(date);
    }

    @PostMapping("/vote")
    public void vote(@RequestBody SimpleVote simpleVote, @AuthenticationPrincipal AuthUser authUser) {
        log.info("vote on {} for {} ", simpleVote.date(), simpleVote.restaurantId());
        voteService.vote(simpleVote.date(), authUser.getUser(), simpleVote.restaurantId());
    }

    public record SimpleVote(LocalDate date, long restaurantId) {
    }

}
