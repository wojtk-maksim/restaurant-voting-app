package ru.javaops.restaurantvoting.error;

public class NotFoundException extends RuntimeException implements HasUri {

    public NotFoundException(String msg) {
        super(msg);
    }

    @Override
    public String getProblemPath() {
        return "/not-found";
    }

}
