package ru.javaops.restaurantvoting.to;

import java.time.LocalDate;
import java.util.Set;

public record DateWithDishes(
        LocalDate date,
        Set<Long> dishes
) {
}
