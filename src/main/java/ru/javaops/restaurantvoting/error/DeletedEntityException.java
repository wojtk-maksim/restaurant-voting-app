package ru.javaops.restaurantvoting.error;

public class DeletedEntityException extends RuntimeException implements HasUri {

    public DeletedEntityException(String msg) {
        super(msg);
    }

    @Override
    public String getProblemPath() {
        return "/deleted";
    }

}
