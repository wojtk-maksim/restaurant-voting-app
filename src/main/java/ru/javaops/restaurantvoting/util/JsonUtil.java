package ru.javaops.restaurantvoting.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class JsonUtil {

    public static ObjectMapper mapper;

    public static void setObjectMapper(ObjectMapper mapper) {
        JsonUtil.mapper = mapper;
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("invalid json");
        }
    }

    public static <T> Collection<T> readValues(String json, Class<T[]> clazz) {
        try {
            return Arrays.asList(mapper.readValue(json, clazz));
        } catch (IOException e) {
            throw new IllegalArgumentException("invalid json");
        }
    }

    public static <T> String writeValue(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("can't convert to json");
        }
    }

}
