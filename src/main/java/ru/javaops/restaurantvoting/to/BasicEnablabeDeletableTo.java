package ru.javaops.restaurantvoting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.javaops.restaurantvoting.Deletable;
import ru.javaops.restaurantvoting.Enablable;
import ru.javaops.restaurantvoting.HasName;
import ru.javaops.restaurantvoting.Views.Public;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class BasicEnablabeDeletableTo extends BasicTo implements Enablable, Deletable, HasName {

    @JsonView(Public.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected boolean enabled;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected boolean deleted;

    public BasicEnablabeDeletableTo(Long id, String name, boolean enabled, boolean deleted) {
        super(id, name);
        this.enabled = enabled;
        this.deleted = deleted;
    }

}
