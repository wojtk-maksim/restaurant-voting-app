package ru.javaops.restaurantvoting.web.validation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.to.BasicTo;
import ru.javaops.restaurantvoting.to.RestaurantTo;
import ru.javaops.restaurantvoting.util.RestaurantUtil;

import static ru.javaops.restaurantvoting.util.RestaurantUtil.RESTAURANT_CODE;

@Component
@Slf4j
@AllArgsConstructor
public class RestaurantUniqueNameValidator implements Validator {

    private final RestaurantRepository restaurantRepository;

    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return RestaurantTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        String name = ((BasicTo) target).getName();
        log.debug("Validate name {} available for restaurant", name);

        // Need to retrieve ID of restaurant. Split request URI by '/':
        // '' / 'api' / 'admin' / 'restaurants' / '{restaurantId}'
        String[] pathMembers = request.getRequestURI().split("/");

        if (pathMembers.length < 5) {
            // It is POST request to create new restaurant
            checkRestaurant(name, errors);
        } else {
            // It is PUT request to update restaurant
            Long restaurantId = Long.valueOf(pathMembers[4]);
            RestaurantUtil.getRestaurantIfExists(restaurantId);
            checkRestaurant(name, errors);
        }
    }

    private void checkRestaurant(String name, Errors errors) {
        if (restaurantRepository.existsByName(name)) {
            errors.rejectValue("name", "error.alreadyExists." + RESTAURANT_CODE, new String[]{name}, "");
        }
    }

}
