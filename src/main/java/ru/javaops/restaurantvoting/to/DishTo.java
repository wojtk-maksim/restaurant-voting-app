package ru.javaops.restaurantvoting.to;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import ru.javaops.restaurantvoting.NamedEnablableDeletable;
import ru.javaops.restaurantvoting.Views.Public;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonPropertyOrder({"id", "name", "price", "enabled", "deleted"})
public class DishTo extends BasicEnablabeDeletableTo implements NamedEnablableDeletable {

    @JsonView(Public.class)
    @Range(message = "{error.range}", min = 5, max = 100)
    private int price;

    public DishTo(Long id, String name, int price, boolean enabled, boolean deleted) {
        super(id, name, enabled, deleted);
        this.price = price;
    }

    public DishTo(Long id, String name, int price) {
        this(id, name, price, true, false);
    }

    @Override
    public String toString() {
        return "[id = " + id + ", name = '" + name + "', price = " + price + "]";
    }

}
