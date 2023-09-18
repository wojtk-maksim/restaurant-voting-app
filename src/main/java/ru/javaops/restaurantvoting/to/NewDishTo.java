package ru.javaops.restaurantvoting.to;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NewDishTo extends AbstractNamedNewDataTo {

    private final Integer price;

    public NewDishTo(@NotBlank String name, @NotNull Integer price) {
        super(name);
        this.price = price;
    }

}
