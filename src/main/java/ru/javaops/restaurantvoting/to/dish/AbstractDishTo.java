package ru.javaops.restaurantvoting.to.dish;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public abstract class AbstractDishTo {

    @NotBlank
    @Size(min = 3, max = 32)
    protected String name;

    protected int price;
}
