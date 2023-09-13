package ru.javaops.restaurantvoting.to.lunch;

import lombok.Getter;
import ru.javaops.restaurantvoting.model.Enablable;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.to.AbstractEntityTo;
import ru.javaops.restaurantvoting.to.dish.DishTo;

import java.util.List;

@Getter
public abstract class AbstractLunchTo extends AbstractEntityTo implements Enablable {

    protected final Restaurant restaurant;

    protected final List<DishTo> dishes;

    protected boolean enabled;

    public AbstractLunchTo(Long id, Restaurant restaurant, List<DishTo> dishes, boolean enabled) {
        super(id);
        this.restaurant = restaurant;
        this.dishes = dishes;
        this.enabled = enabled;
    }

}
