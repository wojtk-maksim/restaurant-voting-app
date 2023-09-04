package ru.javaops.restaurantvoting.error;

public class InvalidVoteException extends RuntimeException {

    public InvalidVoteException(String message) {
        super(message);
    }
}
