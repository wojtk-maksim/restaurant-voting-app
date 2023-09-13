package ru.javaops.restaurantvoting.to;

import lombok.Getter;

@Getter
public abstract class AbstractNamedTo extends AbstractEntityTo {

    protected final String name;

    public AbstractNamedTo(Long id, String name) {
        super(id);
        this.name = name;
    }

}
