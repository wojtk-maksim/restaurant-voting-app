package ru.javaops.restaurantvoting.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.service.VoteService;
import ru.javaops.restaurantvoting.to.LunchTo;
import ru.javaops.restaurantvoting.to.SimpleDishTo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = VoteController.VOTE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {

    public static final String VOTE_URL = "/api/voting/{date}";

    private LunchRepository lunchRepository;

    private VoteService voteService;

    @GetMapping
    public List<LunchTo> getAllOffersOnDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get all on date {}", date);
        return lunchRepository.getAllOnDate(date).stream()
                .map(l -> new LunchTo(
                        date,
                        l.getRestaurant().getId(),
                        l.getDishes().stream()
                                .map(d -> new SimpleDishTo(d.getName(), d.getPrice()))
                                .collect(Collectors.toSet())))
                .collect(Collectors.toList());
    }

    @PostMapping
    public void vote(@PathVariable LocalDate date, @AuthenticationPrincipal AuthUser authUser, @RequestBody int restaurantId) {
        log.info("vote on {} for {} ", restaurantId, date);
        voteService.vote(date, authUser.getUser(), restaurantId);
    }
}
