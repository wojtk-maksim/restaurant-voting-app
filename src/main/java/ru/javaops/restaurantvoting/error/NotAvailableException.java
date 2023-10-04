package ru.javaops.restaurantvoting.error;

public class NotAvailableException extends RuntimeException implements HasUri {

    public NotAvailableException(String msg) {
        super(msg);
    }

    @Override
    public String getProblemPath() {
        return "/not-available";
    }

}
