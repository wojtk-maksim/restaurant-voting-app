package ru.javaops.restaurantvoting.error;

public class DeletedEntityException extends RuntimeException {

    public DeletedEntityException(String object) {
        super(object + " was deleted");
    }

}
