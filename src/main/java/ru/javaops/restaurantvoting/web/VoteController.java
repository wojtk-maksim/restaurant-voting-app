package ru.javaops.restaurantvoting.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.service.VoteService;
import ru.javaops.restaurantvoting.to.lunch.LunchWithVoters;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.restaurantvoting.web.UrlData.API;
import static ru.javaops.restaurantvoting.web.UrlData.VOTING;

@RestController
@RequestMapping(value = VoteController.VOTING_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController extends AbstractRestaurantController {

    public static final String VOTING_URL = API + VOTING;

    private VoteService voteService;

    @GetMapping("/{date}")
    public List<LunchWithVoters> getOffersOnDate(@PathVariable LocalDate date) {
        log.info("get offers on date {}", date);
        return voteService.getOffersOnDate(date);
    }

    @PostMapping("/{date}/vote")
    public void vote(@PathVariable LocalDate date,
                     @RequestBody Long restaurantId,
                     @AuthenticationPrincipal AuthUser authUser) {
        log.info("on {} user {} votes for {}", date, authUser, restaurantId);
        Restaurant restaurant = getRestaurantIfExists(restaurantId);
        voteService.vote(date, authUser.getUser(), restaurant);
    }

}
