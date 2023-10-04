package ru.javaops.restaurantvoting.error;

public class DeadLineException extends RuntimeException implements HasUri {

    public DeadLineException(String msg) {
        super(msg);
    }

    @Override
    public String getProblemPath() {
        return "/deleted";
    }

}
