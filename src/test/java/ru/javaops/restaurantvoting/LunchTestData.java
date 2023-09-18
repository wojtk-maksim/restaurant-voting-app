package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.to.lunch.LunchWithVoters;
import ru.javaops.restaurantvoting.to.lunch.VoterTo;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;
import static ru.javaops.restaurantvoting.UserTestData.USER_ID;
import static ru.javaops.restaurantvoting.UserTestData.user;

public class LunchTestData {

    public static final TestUtil.Matcher<Lunch> LUNCH_MATCHER = new TestUtil.Matcher<>(List.of("id", "dishes.restaurant", "restaurant"));
    public static final TestUtil.Matcher<LunchWithVoters> LUNCH_WITH_VOTERS_MATCHER = new TestUtil.Matcher<>(List.of("voters.restaurantId", "restaurant.dishes", "dishes.restaurant"));

    public static final LocalDate DATE = LocalDate.parse("2020-09-01");

    public static final LocalDate NEW_DATE = LocalDate.now().plusDays(1);

    public static final Lunch burgerKingLunch = new Lunch(1L, DATE, getBurgerKing(), List.of(burger), true);
    public static final Lunch kfcLunch = new Lunch(2L, DATE, kfc, List.of(kfcDish), false);
    public static final Lunch lunchFromUnavailableRestaurant = new Lunch(3L, DATE, unavailableRestaurant, List.of(dishFromUnavailable), true);
    public static final Lunch lunchFromDeletedRestaurant = new Lunch(4L, DATE, deletedRestaurant, List.of(dishFromDeleted), true);
    public static final List<LunchWithVoters> allOffersOnDate = List.of(
            new LunchWithVoters(getBurgerKing(), burgerKingLunch.getDishes(), List.of(new VoterTo(USER_ID, user.getName(), BURGER_KING_ID))),
            new LunchWithVoters(kfc, kfcLunch.getDishes(), null),
            new LunchWithVoters(unavailableRestaurant, null, null),
            new LunchWithVoters(deletedRestaurant, null, null)
    );
    public static final List<LunchWithVoters> lunchesAvailableForVoting = List.of(
            new LunchWithVoters(getBurgerKing(), burgerKingLunch.getDishes(), List.of(new VoterTo(USER_ID, user.getName(), BURGER_KING_ID)))
    );

    public static final List<Lunch> allLunches = List.of(burgerKingLunch, kfcLunch, lunchFromUnavailableRestaurant, lunchFromDeletedRestaurant);
    public static final Lunch newLunch = new Lunch(NEW_DATE, getBurgerKing(), List.of(burger));
    public static final Lunch updatedLunch = new Lunch(NEW_DATE, getBurgerKing(), List.of(cheeseBurger));
    public static final LinkedHashSet<Long> dishIdsForNewLunch = new LinkedHashSet<>() {{
        add(burger.getId());
    }};
    public static final LinkedHashSet<Long> dishIdsForUpdatedLunch = new LinkedHashSet<>() {{
        add(cheeseBurger.getId());
    }};
    public static final List<Dish> dishesForNewLunch = List.of(burger);
    public static final Lunch savedLunch = new Lunch(NEW_DATE, getBurgerKing(), dishesForNewLunch);

}
