package ru.javaops.restaurantvoting.to.restaurant;

import lombok.Getter;
import ru.javaops.restaurantvoting.model.Deletable;
import ru.javaops.restaurantvoting.model.Enablable;
import ru.javaops.restaurantvoting.to.AbstractNamedTo;

@Getter
public class RestaurantTo extends AbstractNamedTo implements Enablable, Deletable {

    private final boolean enabled;

    private final boolean deleted;

    public RestaurantTo(Long id, String name, boolean enabled, boolean deleted) {
        super(id, name);
        this.enabled = enabled;
        this.deleted = deleted;
    }

}
