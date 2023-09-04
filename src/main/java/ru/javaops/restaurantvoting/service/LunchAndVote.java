package ru.javaops.restaurantvoting.service;

import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.model.Vote;

public record LunchAndVote(Lunch lunch, Vote vote) {
}
