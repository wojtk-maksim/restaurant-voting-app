package ru.javaops.restaurantvoting.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class JsonUtil {

    private ObjectMapper mapper;

    public <T> T readValue(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("invalid json");
        }
    }

    public <T> List<T> readValues(String json, Class<T[]> clazz) {
        try {
            return Arrays.asList(mapper.readValue(json, clazz));
        } catch (IOException e) {
            throw new IllegalArgumentException("invalid json");
        }
    }

    public <T> String writeValue(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("can't convert to json");
        }
    }
}
