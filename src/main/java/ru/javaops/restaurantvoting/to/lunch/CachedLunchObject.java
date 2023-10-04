package ru.javaops.restaurantvoting.to.lunch;

import lombok.Value;

import java.util.List;

@Value
// Special object that keeps only IDs of dishes. Real lunch is "built" by retrieving dishes from cache.
public class CachedLunchObject {

    Long id;

    List<Long> dishIds;

    boolean enabled;

}
