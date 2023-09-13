package ru.javaops.restaurantvoting.to.dish;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.javaops.restaurantvoting.to.AbstractNamedNewDataTo;

@Getter
public class NewDishTo extends AbstractNamedNewDataTo {

    private final Integer price;

    public NewDishTo(@NotBlank String name, @NotNull Integer price) {
        super(name);
        this.price = price;
    }

}
