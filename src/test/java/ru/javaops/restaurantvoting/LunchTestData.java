package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.to.DateWithDishes;
import ru.javaops.restaurantvoting.to.VoterTo;
import ru.javaops.restaurantvoting.to.lunch.LunchWithVotersTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static ru.javaops.restaurantvoting.DishTestData.burger;
import static ru.javaops.restaurantvoting.DishTestData.cheeseBurger;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;
import static ru.javaops.restaurantvoting.UserTestData.USER_ID;
import static ru.javaops.restaurantvoting.UserTestData.user;

public class LunchTestData {

    public static final String DATE_AS_STRING = "2020-09-01";
    public static final LocalDate DATE = LocalDate.parse(DATE_AS_STRING);

    public static final String NEW_DATE_AS_STRING = "2020-09-02";
    public static final LocalDate NEW_DATE = LocalDate.parse(NEW_DATE_AS_STRING);

    public static final List<Dish> burgerKingLunchDishes = List.of(burger);

    public static final Lunch burgerKingLunch = new Lunch(DATE, burgerKing, List.of(burger));
    public static final Lunch kfcLunch = new Lunch(DATE, kfc, List.of(new Dish(3L, "bucket", 300, kfc)));
    public static final Lunch subwayLunch = new Lunch(DATE, subway, List.of(new Dish(5L, "subway club", 200, subway)));
    public static final Lunch dodoPizzaLunch = new Lunch(DATE, dodoPizza, List.of(new Dish(7L, "dodo", 300, dodoPizza)));
    public static final Lunch newLunch = new Lunch(NEW_DATE, burgerKing, List.of(burger));
    public static final Lunch updatedLunch = new Lunch(DATE, burgerKing, List.of(cheeseBurger));

    public static final Set<Long> dishIdsForNewLunch = Set.of(cheeseBurger.getId());
    public static final Set<Long> dishIdsForUpdatedLunch = Set.of(cheeseBurger.getId());

    public static final List<Dish> dishesForNewLunch = List.of(cheeseBurger);
    public static final DateWithDishes NEW_LUNCH_DATA = new DateWithDishes(NEW_DATE, dishIdsForNewLunch);
    public static final Lunch savedLunch = new Lunch(NEW_DATE, burgerKing, dishesForNewLunch);

    public static final List<Lunch> lunches = List.of(burgerKingLunch, kfcLunch, subwayLunch, dodoPizzaLunch);

    public static final List<LunchWithVotersTo> lunchesOnDate = List.of(
            new LunchWithVotersTo(burgerKing, List.of(new VoterTo(USER_ID, user.getName(), 1L))),
            new LunchWithVotersTo(kfc, null),
            new LunchWithVotersTo(subway, null),
            new LunchWithVotersTo(dodoPizza, null)

    );

}
