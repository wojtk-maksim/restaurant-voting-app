package ru.javaops.restaurantvoting.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.javaops.restaurantvoting.error.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static ru.javaops.restaurantvoting.error.ErrorType.*;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class RestExceptionHandler {

    private static final Map<Class<? extends Throwable>, ErrorType> httpStatusMap = new LinkedHashMap<>() {
        {
            put(NotFoundException.class, NOT_FOUND);
            put(NotAvailableException.class, NOT_AVAILABLE);
            put(DeletedEntityException.class, DELETED);
            put(DeadLineException.class, DEADLINE_ERROR);
            put(BannedUserException.class, BANNED_USER);
            put(BindException.class, BAD_REQUEST);
            put(AuthenticationException.class, UNAUTHORIZED);
        }
    };

    @ExceptionHandler(BindException.class)
    ProblemDetail bindException(BindException ex, HttpServletRequest request) {
        Map<String, String> invalidParams = new LinkedHashMap<>();
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            invalidParams.put(error.getObjectName(), error.getDefaultMessage());
        }
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            invalidParams.put(error.getField(), error.getDefaultMessage());
        }
        return processException(ex, request, invalidParams);
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail exception(Exception ex, HttpServletRequest request) {
        return processException(ex, request, Map.of());
    }

    private ProblemDetail processException(Exception ex, HttpServletRequest request, Map<String, String> additionalParams) {
        String path = request.getRequestURI();
        Optional<ErrorType> errorType = httpStatusMap.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(ex.getClass()))
                .map(Map.Entry::getValue)
                .findAny();
        if (errorType.isPresent()) {
            return createProblemDetail(ex, errorType.get(), "Exception " + ex.getClass().getSimpleName(), additionalParams);
        } else {
            return createProblemDetail(ex, APP_ERROR, "Exception " + ex.getClass().getSimpleName(), additionalParams);
        }
    }

    private ProblemDetail createProblemDetail(Exception ex, ErrorType errorType, String defaultDetail, Map<String, String> additionalParams) {
        ErrorResponse.Builder builder = ErrorResponse.builder(ex, errorType.status, defaultDetail);
        ProblemDetail problemDetail = builder.build().getBody();
        additionalParams.forEach(problemDetail::setProperty);
        return problemDetail;
    }

}
