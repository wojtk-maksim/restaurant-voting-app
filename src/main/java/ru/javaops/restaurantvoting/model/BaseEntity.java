package ru.javaops.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javaops.restaurantvoting.HasId;
import ru.javaops.restaurantvoting.Views.Public;

@MappedSuperclass
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity implements HasId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Public.class)
    protected Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseEntity that)) {
            return false;
        }
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

}
