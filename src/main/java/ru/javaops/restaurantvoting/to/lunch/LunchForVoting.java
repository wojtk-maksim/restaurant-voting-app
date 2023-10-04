package ru.javaops.restaurantvoting.to.lunch;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import ru.javaops.restaurantvoting.to.DishTo;
import ru.javaops.restaurantvoting.to.RestaurantTo;
import ru.javaops.restaurantvoting.to.user.VoterTo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"restaurant", "dishes", "enabled", "voters"})
public class LunchForVoting extends LunchTo {

    private RestaurantTo restaurant;

    private List<VoterTo> voters;

    public LunchForVoting(RestaurantTo restaurant, Long id, List<DishTo> dishes, boolean enabled, List<VoterTo> voters) {
        super(id, dishes, enabled);
        this.restaurant = restaurant;
        this.voters = voters;
    }

    @Override
    public String toString() {
        return "[id = " + id + ", restaurant = " + restaurant + ", dishes = " + dishes + ", voters = " + voters + "]";
    }

}
