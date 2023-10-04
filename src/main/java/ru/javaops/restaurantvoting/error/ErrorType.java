package ru.javaops.restaurantvoting.error;

import org.springframework.http.HttpStatus;

public enum ErrorType {

    AUTH_NOT_FOUND("problem.title.notFound", HttpStatus.UNAUTHORIZED),
    AUTH_EXPIRED("problem.title.authExpired", HttpStatus.UNAUTHORIZED),
    AUTH_BANNED("problem.title.authBanned", HttpStatus.FORBIDDEN),
    AUTH_DELETED("problem.title.deleted", HttpStatus.UNAUTHORIZED),
    NOT_FOUND("problem.title.notFound", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS("problem.title.alreadyExists", HttpStatus.CONFLICT),
    NOT_AVAILABLE("problem.title.notAvailable", HttpStatus.CONFLICT),
    DELETED("problem.title.deleted", HttpStatus.GONE),
    DEADLINE_ERROR("problem.title.deadline", HttpStatus.LOCKED),
    BAD_REQUEST("problem.title.badRequest", HttpStatus.UNPROCESSABLE_ENTITY),
    BAD_DATA("problem.title.badData", HttpStatus.UNPROCESSABLE_ENTITY),
    UNAUTHORIZED("problem.title.unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("problem.title.forbidden", HttpStatus.FORBIDDEN),
    DATA_CONFLICT("problem.title.dataConflict", HttpStatus.CONFLICT),
    APP_ERROR("problem.title.appError", HttpStatus.INTERNAL_SERVER_ERROR);

    ErrorType(String title, HttpStatus status) {
        this.title = title;
        this.status = status;
    }

    public final String title;

    public final HttpStatus status;

}
