package ru.javaops.restaurantvoting.util.validation;

import ru.javaops.restaurantvoting.error.NotAvailableException;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.Enableable;

public class ValidationUtil {

    public static <T> T checkExists(T obj) {
        if (obj == null) {
            throw new NotFoundException();
        }
        return obj;
    }

    public static void checkAvailable(Enableable obj) {
        if (!obj.isEnabled()) {
            throw new NotAvailableException();
        }
    }

}
