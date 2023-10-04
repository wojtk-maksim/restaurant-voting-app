package ru.javaops.restaurantvoting.to.lunch.new_data;

import java.util.List;

public interface HasDishIds {

    List<Long> getDishes();

    Long getDishId(int index);

}
