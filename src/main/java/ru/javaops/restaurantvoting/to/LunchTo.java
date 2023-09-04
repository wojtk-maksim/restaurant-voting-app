package ru.javaops.restaurantvoting.to;

import java.time.LocalDate;
import java.util.Set;

public record LunchTo(LocalDate date, int restaurantId, Set<SimpleDishTo> dishes) {
}
