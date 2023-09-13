package ru.javaops.restaurantvoting.error;

public class EmailOccupiedException extends RuntimeException {

    public EmailOccupiedException(String email) {
        super(email + " is already registered");
    }

}
