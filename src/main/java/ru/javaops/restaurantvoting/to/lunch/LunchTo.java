package ru.javaops.restaurantvoting.to.lunch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javaops.restaurantvoting.to.DishTo;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LunchTo {

    @JsonIgnore
    protected Long id;

    protected List<DishTo> dishes;

    private boolean enabled;

    @Override
    public String toString() {
        return "[id = " + id + ", dishes = " + dishes + "]";
    }

}
