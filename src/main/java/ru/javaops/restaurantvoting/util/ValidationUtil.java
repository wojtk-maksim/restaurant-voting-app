package ru.javaops.restaurantvoting.util;

import org.springframework.context.support.MessageSourceAccessor;
import ru.javaops.restaurantvoting.HasName;
import ru.javaops.restaurantvoting.error.NotFoundException;

public class ValidationUtil {

    public static MessageSourceAccessor messageSourceAccessor;

    public static void setMessageSource(MessageSourceAccessor messageSourceAccessor) {
        ValidationUtil.messageSourceAccessor = messageSourceAccessor;
    }

    static void checkFound(String codeName, Object object, Object identifier) {
        if (object == null) {
            throw new NotFoundException(messageSourceAccessor.getMessage(
                    "error.notFound." + codeName, new String[]{identifier.toString()})
            );
        }
    }

    static void checkFoundInRestaurant(String codeName, Object object, Object identifier, HasName restaurant) {
        if (object == null) {
            throw new NotFoundException(messageSourceAccessor.getMessage(
                    "error.notFound." + codeName, new String[]{identifier.toString(), restaurant.getName()})
            );
        }
    }

}
