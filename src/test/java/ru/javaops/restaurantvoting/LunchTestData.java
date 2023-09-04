package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.to.LunchTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;
import static ru.javaops.restaurantvoting.to.ToConverter.lunchTo;

public class LunchTestData {

    public static final String DATE_AS_STRING = "2020-09-01";
    public static final LocalDate DATE = LocalDate.parse(DATE_AS_STRING);

    public static final String NEW_DATE_AS_STRING = "2020-09-02";
    public static final LocalDate NEW_DATE = LocalDate.parse(NEW_DATE_AS_STRING);

    public static final Set<Dish> burgerKingLunchDishes = Set.of(burger);

    public static final LunchTo burgerKingLunch = lunchTo(new Lunch(DATE, burgerKing, Set.of(burger)));
    public static final LunchTo kfcLunch = lunchTo(new Lunch(DATE, kfc, Set.of(new Dish(3, "bucket", 300, kfc))));
    public static final LunchTo subwayLunch = lunchTo(new Lunch(DATE, subway, Set.of(new Dish(5, "subway club", 200, subway))));
    public static final LunchTo dodoPizzaLunch = lunchTo(new Lunch(DATE, dodoPizza, Set.of(new Dish(7, "dodo", 300, dodoPizza))));
    public static final LunchTo newLunch = lunchTo(new Lunch(NEW_DATE, burgerKing, Set.of(burger)));
    public static final LunchTo updatedLunch = lunchTo(new Lunch(DATE, burgerKing, Set.of(cheeseBurger)));

    public static final Set<Integer> dishIdsForNewLunch = Set.of(BURGER_ID);
    public static final Set<Integer> dishIdsForUpdatedLunch = Set.of(cheeseBurger.getId());

    public static final Set<Dish> dishesForNewLunch = Set.of(burger);

    public static final List<LunchTo> lunches = List.of(burgerKingLunch, kfcLunch, subwayLunch, dodoPizzaLunch);
}
