package ru.javaops.restaurantvoting.to.lunch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;
import ru.javaops.restaurantvoting.model.Lunch;

import java.util.List;

@Value
public class LunchWithVotersTo {

    @JsonIgnoreProperties({"date", "restaurant.dishes", "dishes.restaurant", "enabled"})
    Lunch lunch;

    @JsonIgnoreProperties("lunchId")
    List<VoterTo> voters;

    public LunchWithVotersTo(Lunch lunch, List<VoterTo> voters) {
        this.lunch = lunch;
        this.voters = voters;
    }

}
