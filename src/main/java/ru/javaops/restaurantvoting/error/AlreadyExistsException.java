package ru.javaops.restaurantvoting.error;

public class AlreadyExistsException extends RuntimeException implements HasUri {

    public AlreadyExistsException(String msg) {
        super(msg);
    }

    @Override
    public String getProblemPath() {
        return "/already-exists";
    }

}
