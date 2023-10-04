package ru.javaops.restaurantvoting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.javaops.restaurantvoting.Views.Public;
import ru.javaops.restaurantvoting.web.validation.NoHtml;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasicTo {

    @JsonView(Public.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Long id;

    @JsonView(Public.class)
    @NotBlank(message = "{error.notBlank}")
    @Size(message = "{error.size}", min = 2, max = 32)
    @NoHtml
    protected String name;

    @Override
    public String toString() {
        return "[id = " + id + ", name = '" + name + "']";
    }

}
