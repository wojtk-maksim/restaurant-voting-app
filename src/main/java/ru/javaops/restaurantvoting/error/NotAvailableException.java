package ru.javaops.restaurantvoting.error;

public class NotAvailableException extends RuntimeException {

    public NotAvailableException(String object) {
        super(object + " not available");
    }

}
