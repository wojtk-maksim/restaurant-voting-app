package ru.javaops.restaurantvoting.error;

public class DeadLineException extends RuntimeException {

    public DeadLineException(String action) {
        super("forbidden to " + action + " after deadline");
    }

}
