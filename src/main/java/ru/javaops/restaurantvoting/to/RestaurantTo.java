package ru.javaops.restaurantvoting.to;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.javaops.restaurantvoting.NamedEnablableDeletable;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonPropertyOrder({"id", "name", "enabled", "deleted"})
public class RestaurantTo extends BasicEnablabeDeletableTo implements NamedEnablableDeletable {

    public RestaurantTo(Long id, String name, boolean enabled, boolean deleted) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.deleted = deleted;
    }

    public RestaurantTo(Long id, String name) {
        this(id, name, true, false);
    }

    @Override
    public String toString() {
        return "[id = " + id + ", name = '" + name + "']";
    }

}
