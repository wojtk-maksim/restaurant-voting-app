package ru.javaops.restaurantvoting.to.restaurant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.javaops.restaurantvoting.to.AbstractNamedNewDataTo;

public class NewRestaurantTo extends AbstractNamedNewDataTo {

    // Can't deserialize when class has only one field
    //https://stackoverflow.com/questions/69538090/json-parse-error-cannot-construct-instance-of-although-at-least-one-creator-e
    @JsonCreator
    public NewRestaurantTo(@JsonProperty("name") String name) {
        super(name);
    }

}
