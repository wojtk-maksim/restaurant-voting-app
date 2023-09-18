package ru.javaops.restaurantvoting.to.lunch;

import lombok.AllArgsConstructor;
import ru.javaops.restaurantvoting.model.Lunch;

import java.util.List;

@AllArgsConstructor
public class LunchAndVoters {

    private Lunch lunch;

    private List<VoterTo> voters;

    public void addVoter(VoterTo voter) {
        voters.add(voter);
    }

}
