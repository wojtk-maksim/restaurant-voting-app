package ru.javaops.restaurantvoting.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.restaurantvoting.error.*;
import ru.javaops.restaurantvoting.error.authentication.BannedAuthenticationException;
import ru.javaops.restaurantvoting.error.authentication.DeletedAuthenticationException;
import ru.javaops.restaurantvoting.error.authentication.ExpiredAuthenticationException;
import ru.javaops.restaurantvoting.error.authentication.NotFoundAuthenticationException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ru.javaops.restaurantvoting.web.UrlData.API_PATH;
import static ru.javaops.restaurantvoting.web.UrlData.PROBLEMS_PATH;

@RestController
@RequestMapping(value = ProblemsController.PROBLEMS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ProblemsController {

    public static final String PROBLEMS_URL = API_PATH + PROBLEMS_PATH;

    private static final String PROBLEMS_CODE = "problem";

    private final MessageSourceAccessor messageAccessor;

    private final Map<HasUri, String> exceptionDescriptionCodes = new LinkedHashMap<>() {{
        put(new NotFoundAuthenticationException(""), PROBLEMS_CODE + ".auth.notFound");
        put(new ExpiredAuthenticationException(""), PROBLEMS_CODE + ".auth.expired");
        put(new DeletedAuthenticationException(""), PROBLEMS_CODE + ".auth.deleted");
        put(new BannedAuthenticationException(""), PROBLEMS_CODE + ".auth.banned");
        put(new NotFoundException(""), PROBLEMS_CODE + ".notFound");
        put(new AlreadyExistsException(""), PROBLEMS_CODE + ".alreadyExists");
        put(new NotAvailableException(""), PROBLEMS_CODE + ".notAvailable");
        put(new DeletedEntityException(""), PROBLEMS_CODE + ".deleted");
        put(new DeadLineException(""), PROBLEMS_CODE + ".deadline");
    }};

    @GetMapping
    public List<ExceptionDescription> getAll() {
        log.info("Get all exceptions with descriptions");
        return exceptionDescriptionCodes.entrySet().stream()
                .map(entry -> new ExceptionDescription(
                        entry.getKey().getClass().getSimpleName(),
                        messageAccessor.getMessage(entry.getValue())
                ))
                .toList();
    }

    @GetMapping("/{problemPath}")
    public ExceptionDescription get(@PathVariable String problemPath) {
        log.info("Get exception with description '{}'", problemPath);
        String fullProblemPath = "/" + problemPath;
        return exceptionDescriptionCodes.entrySet().stream()
                .filter(entry -> fullProblemPath.equals(entry.getKey().getProblemPath()))
                .map(entry -> new ExceptionDescription(
                        entry.getKey().getClass().getSimpleName(),
                        messageAccessor.getMessage(entry.getValue())
                ))
                .findAny()
                .orElse(null);
    }

    public record ExceptionDescription(String exceptionName, String description) {
    }

}
