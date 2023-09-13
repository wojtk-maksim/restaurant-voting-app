package ru.javaops.restaurantvoting.to.dish;

import lombok.Getter;
import ru.javaops.restaurantvoting.model.Deletable;
import ru.javaops.restaurantvoting.model.Enablable;
import ru.javaops.restaurantvoting.to.AbstractNamedTo;

@Getter
public class DishTo extends AbstractNamedTo implements Enablable, Deletable {

    private final int price;

    private final boolean enabled;

    private final boolean deleted;

    public DishTo(Long id, String name, int price, boolean enabled, boolean deleted) {
        super(id, name);
        this.price = price;
        this.enabled = enabled;
        this.deleted = deleted;
    }

}
