package ru.javaops.restaurantvoting.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.config.AuthToken;
import ru.javaops.restaurantvoting.service.VoteAndLunchService;
import ru.javaops.restaurantvoting.to.lunch.LunchForVoting;
import ru.javaops.restaurantvoting.to.lunch.new_data.DishesForMultipleLunches;
import ru.javaops.restaurantvoting.web.validation.DishesForLunchValidator;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

import static ru.javaops.restaurantvoting.web.UrlData.API_PATH;
import static ru.javaops.restaurantvoting.web.UrlData.LUNCHES_PATH;

@RestController
@RequestMapping(value = VoteAndLunchController.LUNCH_VOTING_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteAndLunchController {

    public static final String LUNCH_VOTING_URL = API_PATH + LUNCHES_PATH + "/{date}";

    private final VoteAndLunchService voteAndLunchService;

    private DishesForLunchValidator dishesForLunchValidator;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(dishesForLunchValidator);
    }

    @GetMapping
    public List<LunchForVoting> getLunchesOnDate(@PathVariable LocalDate date) {
        log.info("Get lunches on {}", date);
        return voteAndLunchService.getLunchesForVoting(date);
    }

    @PostMapping("vote")
    public void vote(@PathVariable LocalDate date, @RequestParam Long restaurantId,
                     @AuthenticationPrincipal AuthToken authUser) {
        log.info("On {} user {} votes for restaurant {}", date, authUser, restaurantId);
        voteAndLunchService.vote(date, authUser.getId(), restaurantId);
    }

    @PreAuthorize("hasAuthority('ADMIN_ACCESS')")
    @PostMapping
    public void batchCreateOrUpdateLunchesOnDate(@PathVariable LocalDate date,
                                                 @RequestBody @Valid DishesForMultipleLunches dishesForMultipleLunches,
                                                 @AuthenticationPrincipal AuthToken authUser) {

        log.info("On {} batch create or update lunches by admin {}. Lunches by restaurants: {}", date, authUser, dishesForMultipleLunches);
        LinkedHashMap<Long, List<Long>> dishIdsByRestaurantIds = dishesForMultipleLunches.getDishIdsByRestaurantIds();
        List<Long> allRestaurantIds = dishesForMultipleLunches.getAllRestaurantIds();
        List<Long> allDishIds = dishesForMultipleLunches.getAllDishIds();
        voteAndLunchService.batchCreateOrUpdate(date, dishIdsByRestaurantIds, allRestaurantIds, allDishIds);
    }

}
