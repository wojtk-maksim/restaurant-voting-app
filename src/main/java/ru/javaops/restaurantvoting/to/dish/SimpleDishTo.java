package ru.javaops.restaurantvoting.to.dish;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class SimpleDishTo extends AbstractDishTo {

    public SimpleDishTo(String name, int price) {
        super(name, price);
    }
}
