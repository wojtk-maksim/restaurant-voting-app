package ru.javaops.restaurantvoting.error;

import org.springframework.http.HttpStatus;

public enum ErrorType {

    NOT_FOUND("Invalid Request Data", HttpStatus.NOT_FOUND),
    NOT_AVAILABLE("Unavailable Request Data", HttpStatus.CONFLICT),
    DELETED("Deleted Request Data", HttpStatus.GONE),
    DEADLINE_ERROR("Past the Deadline Request", HttpStatus.LOCKED),
    BANNED_USER("Banned", HttpStatus.FORBIDDEN),
    BAD_REQUEST("Bad Request", HttpStatus.UNPROCESSABLE_ENTITY),
    UNAUTHORIZED("Unauthorized", HttpStatus.UNAUTHORIZED),
    APP_ERROR("Application Error", HttpStatus.INTERNAL_SERVER_ERROR);

    ErrorType(String title, HttpStatus status) {
        this.title = title;
        this.status = status;
    }

    public final String title;

    public final HttpStatus status;

}
