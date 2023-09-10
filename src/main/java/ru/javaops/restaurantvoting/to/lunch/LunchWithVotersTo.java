package ru.javaops.restaurantvoting.to.lunch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.to.VoterTo;

import java.util.List;

@Data
@AllArgsConstructor
public class LunchWithVotersTo {

    @JsonIgnoreProperties({"dishes", "enabled"})
    private Restaurant restaurant;

    @JsonIgnoreProperties("lunchId")
    private List<VoterTo> voters;

    public void addVoter(VoterTo voter) {
        voters.add(voter);
    }
}
