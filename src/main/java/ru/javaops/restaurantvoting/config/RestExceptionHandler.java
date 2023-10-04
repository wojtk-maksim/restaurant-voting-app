package ru.javaops.restaurantvoting.config;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.javaops.restaurantvoting.error.*;
import ru.javaops.restaurantvoting.error.authentication.BannedAuthenticationException;
import ru.javaops.restaurantvoting.error.authentication.DeletedAuthenticationException;
import ru.javaops.restaurantvoting.error.authentication.ExpiredAuthenticationException;
import ru.javaops.restaurantvoting.error.authentication.NotFoundAuthenticationException;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;
import static ru.javaops.restaurantvoting.error.ErrorType.*;
import static ru.javaops.restaurantvoting.web.UrlData.PROBLEMS_PATH;

@Component
@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class RestExceptionHandler {

    private final MessageSource messageSource;

    private final MessageSourceAccessor messageSourceAccessor;

    private static final Map<Class<? extends Throwable>, ErrorType> httpStatusMap = new LinkedHashMap<>() {
        {
            // Common exceptions.
            put(NotFoundException.class, NOT_FOUND);
            put(NotAvailableException.class, NOT_AVAILABLE);
            put(DeletedEntityException.class, DELETED);
            put(DeadLineException.class, DEADLINE_ERROR);
            put(AlreadyExistsException.class, ALREADY_EXISTS);

            // Authentication exceptions.
            put(NotFoundAuthenticationException.class, AUTH_NOT_FOUND);
            put(ExpiredAuthenticationException.class, AUTH_EXPIRED);
            put(DeletedAuthenticationException.class, AUTH_DELETED);
            put(BannedAuthenticationException.class, AUTH_BANNED);

            put(BindException.class, BAD_REQUEST);
            put(NoHandlerFoundException.class, NOT_FOUND);
            put(IllegalArgumentException.class, BAD_DATA);
            put(UnsupportedOperationException.class, APP_ERROR);
            put(EntityNotFoundException.class, DATA_CONFLICT);
            put(DataIntegrityViolationException.class, DATA_CONFLICT);
            put(ValidationException.class, BAD_REQUEST);
            put(HttpRequestMethodNotSupportedException.class, BAD_REQUEST);
            put(MissingServletRequestParameterException.class, BAD_REQUEST);
            put(RequestRejectedException.class, BAD_REQUEST);
            put(AccessDeniedException.class, FORBIDDEN);
            put(FileNotFoundException.class, NOT_FOUND);
            put(AuthenticationException.class, UNAUTHORIZED);
        }
    };

    @ExceptionHandler(BindException.class)
    ProblemDetail bindException(BindException ex, HttpServletRequest request) {
        Map<String, String> invalidParams = new LinkedHashMap<>();
        ex.getBindingResult().getGlobalErrors().forEach(error -> invalidParams.put(
                error.getObjectName(),
                messageSourceAccessor.getMessage(error.getCode(), error.getArguments(), error.getDefaultMessage())
        ));
        ex.getBindingResult().getFieldErrors().forEach(error -> invalidParams.put(
                error.getField(),
                messageSourceAccessor.getMessage(error.getCode(), error.getArguments(), error.getDefaultMessage())
        ));
        return processException(ex, messageSourceAccessor.getMessage("problem.detail.validationFailed"), request, Map.of("invalidParams", invalidParams));
    }

    // https://howtodoinjava.com/spring-mvc/spring-problemdetail-errorresponse/#5-adding-problemdetail-to-custom-exceptions
    @ExceptionHandler(Exception.class)
    ProblemDetail exception(Exception ex, HttpServletRequest request) {
        String localizedMessage = ex.getLocalizedMessage();
        return processException(ex, localizedMessage != null ? localizedMessage : ex.getMessage(), request, null);
    }

    public ProblemDetail processException(Exception ex, String message, HttpServletRequest request, Map<String, Object> additionalParams) {
        String path = request.getRequestURI();
        Optional<ErrorType> errorType = httpStatusMap.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(ex.getClass()))
                .map(Map.Entry::getValue)
                .findAny();
        if (errorType.isPresent()) {
            log.error("Exception {} at request {}", ex, path);
            return createProblemDetail(ex, errorType.get(), message, request, additionalParams);
        } else {
            Throwable rootCause = getRootCause(ex);
            log.error("Exception {} at request {}", ex, path);
            return createProblemDetail(rootCause, APP_ERROR, "Exception " + rootCause.getClass().getSimpleName(), request, additionalParams);
        }
    }

    private ProblemDetail createProblemDetail(Throwable ex, ErrorType errorType, String defaultDetail,
                                              HttpServletRequest request, Map<String, Object> additionalParams) {
        ErrorResponse.Builder responseBuilder = ErrorResponse.builder(ex, errorType.status, defaultDetail);
        ProblemDetail problemDetail = responseBuilder.build().updateAndGetBody(messageSource, getLocale());
        problemDetail.setTitle(messageSourceAccessor.getMessage(errorType.title));
        if (HasUri.class.isAssignableFrom(ex.getClass())) {
            problemDetail.setType(URI.create(
                    request.getContextPath() + PROBLEMS_PATH + ((HasUri) ex).getProblemPath()
            ));
        }
        if (additionalParams != null) {
            additionalParams.forEach(problemDetail::setProperty);
        }
        return problemDetail;
    }

    @NonNull
    private static Throwable getRootCause(Throwable ex) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(ex);
        return rootCause != null ? rootCause : ex;
    }

}
