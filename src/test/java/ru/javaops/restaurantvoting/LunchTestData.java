package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.to.lunch.LunchWithVotersTo;
import ru.javaops.restaurantvoting.to.lunch.VoterTo;
import ru.javaops.restaurantvoting.web.admin.AdminLunchController;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;
import static ru.javaops.restaurantvoting.UserTestData.USER_ID;
import static ru.javaops.restaurantvoting.UserTestData.user;

public class LunchTestData {

    public static final TestUtil.Matcher<Lunch> LUNCH_MATCHER = new TestUtil.Matcher<>(List.of("id", "dishes.restaurant", "restaurant.dishes"));
    public static final TestUtil.Matcher<LunchWithVotersTo> LUNCH_WITH_VOTERS_MATCHER = new TestUtil.Matcher<>(List.of("lunch.date", "lunch.restaurant.dishes", "lunch.dishes.restaurant", "lunch.enabled", "voters.restaurantId"));

    public static final LocalDate DATE = LocalDate.parse("2020-09-01");

    public static final LocalDate NEW_DATE = LocalDate.now().plusDays(1);

    public static final Lunch burgerKingLunch = new Lunch(1L, DATE, burgerKing, List.of(burger), true);
    public static final List<LunchWithVotersTo> lunchesAvailableForVoting = List.of(
            new LunchWithVotersTo(burgerKingLunch, List.of(new VoterTo(USER_ID, user.getName(), BURGER_KING_ID)))
    );
    public static final Lunch kfcLunch = new Lunch(2L, DATE, kfc, List.of(kfcDish), false);
    public static final Lunch lunchFromUnavailableRestaurant = new Lunch(3L, DATE, unavailableRestaurant, List.of(dishFromUnavailable), true);
    public static final Lunch lunchFromDeletedRestaurant = new Lunch(4L, DATE, deletedRestaurant, List.of(dishFromDeleted), true);
    public static final List<Lunch> allLunches = List.of(burgerKingLunch, kfcLunch, lunchFromUnavailableRestaurant, lunchFromDeletedRestaurant);
    public static final Lunch newLunch = new Lunch(NEW_DATE, burgerKing, List.of(burger));
    public static final Lunch updatedLunch = new Lunch(NEW_DATE, burgerKing, List.of(cheeseBurger));
    public static final LinkedHashSet<Long> dishIdsForNewLunch = new LinkedHashSet<>() {{
        add(burger.getId());
    }};
    public static final LinkedHashSet<Long> dishIdsForUpdatedLunch = new LinkedHashSet<>() {{
        add(cheeseBurger.getId());
    }};
    public static final List<Dish> dishesForNewLunch = List.of(burger);
    public static final AdminLunchController.DateWithDishes NEW_LUNCH_DATA = new AdminLunchController.DateWithDishes(NEW_DATE, dishIdsForNewLunch);
    public static final Lunch savedLunch = new Lunch(NEW_DATE, burgerKing, dishesForNewLunch);

}
