package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.to.lunch.LunchForVoting;
import ru.javaops.restaurantvoting.to.lunch.LunchTo;
import ru.javaops.restaurantvoting.to.lunch.new_data.DishesForMultipleLunches;
import ru.javaops.restaurantvoting.to.lunch.new_data.DishesForSingleLunch;
import ru.javaops.restaurantvoting.to.lunch.new_data.RestaurantDishesPair;
import ru.javaops.restaurantvoting.to.user.VoterTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;
import static ru.javaops.restaurantvoting.UserTestData.USER_ID;
import static ru.javaops.restaurantvoting.UserTestData.user;
import static ru.javaops.restaurantvoting.model.Role.USER;

public class LunchTestData {

    public static final Matcher<Lunch> LUNCH_MATCHER = new Matcher<>(Lunch.class, "restaurant.name", "restaurant.dishes", "dishes.restaurant", "dishes.name", "dishes.price");
    public static final Matcher<LunchTo> LUNCH_TO_MATCHER = new Matcher<>(LunchTo.class, "id");
    public static final Matcher<LunchForVoting> LUNCH_WITH_VOTERS_MATCHER = new Matcher<>(LunchForVoting.class, "id", "restaurant.dishes", "dishes.restaurant", "voters.lunchId", "voters.enabled", "voters.deleted");

    public static final LocalDate DATE = LocalDate.parse("2023-09-01");

    public static final LocalDate NEW_DATE = LocalDate.now().plusDays(1);

    public static final LunchTo BURGER_KING_LUNCH = new LunchTo(1L, List.of(BURGER), true);
    public static final LunchTo KFC_LUNCH = new LunchTo(2L, List.of(KFC_DISH), false);
    public static final LunchTo LUNCH_FROM_UNAVAILABLE = new LunchTo(3L, List.of(DISH_FROM_UNAVAILABLE), true);
    public static final LunchTo LUNCH_FROM_DELETED = new LunchTo(4L, List.of(DISH_FROM_DELETED), true);
    public static final List<LunchForVoting> allLunchesOnDate = List.of(
            new LunchForVoting(BURGER_KING, BURGER_KING_LUNCH.getId(), BURGER_KING_LUNCH.getDishes(), BURGER_KING_LUNCH.isEnabled(), List.of(new VoterTo(USER_ID, user.getName(), USER, true, false, 1L))),
            new LunchForVoting(KFC, KFC_LUNCH.getId(), KFC_LUNCH.getDishes(), KFC_LUNCH.isEnabled(), null),
            new LunchForVoting(UNAVAILABLE_RESTAURANT, LUNCH_FROM_UNAVAILABLE.getId(), LUNCH_FROM_UNAVAILABLE.getDishes(), LUNCH_FROM_UNAVAILABLE.isEnabled(), null),
            new LunchForVoting(DELETED_RESTAURANT, LUNCH_FROM_DELETED.getId(), LUNCH_FROM_DELETED.getDishes(), LUNCH_FROM_DELETED.isEnabled(), null)
    );

    public static final Lunch newLunch = new Lunch(NEW_DATE, burgerKing, List.of(burger));
    public static final LunchTo NEW_LUNCH = new LunchTo(null, List.of(BURGER), true);
    public static final Lunch updatedLunch = new Lunch(NEW_DATE, burgerKing, List.of(cheeseBurger));
    public static final LunchTo UPDATED_LUNCH = new LunchTo(BURGER_KING_LUNCH.getId(), List.of(CHEESEBURGER), true);
    public static final List<Long> dishIdsForNewLunch = List.of(BURGER_ID);
    public static final List<Long> dishIdsForUpdatedLunch = List.of(CHEESEBURGER.getId());
    public static final DishesForSingleLunch DISHES_FOR_NEW_LUNCH = new DishesForSingleLunch(dishIdsForNewLunch);
    public static final DishesForSingleLunch DISHES_FOR_UPDATED_LUNCH = new DishesForSingleLunch(dishIdsForUpdatedLunch);

    public static final DishesForMultipleLunches DISHES_FOR_MULTIPLE_LUNCHES;
    public static final List<Lunch> newLunches = List.of(
            new Lunch(NEW_DATE, burgerKing, List.of(burger, cheeseBurger)),
            new Lunch(NEW_DATE, kfc, List.of(kfcDish))
    );

    static {
        List<RestaurantDishesPair> restaurantDishesPairs = new ArrayList<>();
        restaurantDishesPairs.add(new RestaurantDishesPair(BURGER_KING_ID, List.of(BURGER_ID, CHEESEBURGER.getId())));
        restaurantDishesPairs.add(new RestaurantDishesPair(KFC.getId(), List.of(KFC_DISH.getId())));
        DISHES_FOR_MULTIPLE_LUNCHES = new DishesForMultipleLunches();
        DISHES_FOR_MULTIPLE_LUNCHES.setLunches(restaurantDishesPairs);
    }

    public static final List<LunchForVoting> allBatchSavedLunches = new ArrayList<>() {
        {
            add(new LunchForVoting(BURGER_KING, null, List.of(BURGER, CHEESEBURGER), true, null));
            add(new LunchForVoting(KFC, null, List.of(KFC_DISH), true, null));
        }
    };

}
