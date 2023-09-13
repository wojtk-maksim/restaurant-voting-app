package ru.javaops.restaurantvoting.error;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String object) {
        super(object + " not found");
    }

}
