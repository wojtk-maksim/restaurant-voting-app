package ru.javaops.restaurantvoting.to;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbstractNamedNewDataTo {

    @NotBlank
    protected final String name;

}
