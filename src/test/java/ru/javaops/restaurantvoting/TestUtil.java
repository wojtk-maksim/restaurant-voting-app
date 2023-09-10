package ru.javaops.restaurantvoting;

import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javaops.restaurantvoting.util.JsonUtil.readValue;
import static ru.javaops.restaurantvoting.util.JsonUtil.readValues;

public class TestUtil {

    public static String extractJson(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public static <T> void matches(T actual, T expected, String... fieldsToIgnore) {
        assertThat(actual).usingRecursiveComparison().ignoringFields(fieldsToIgnore).isEqualTo(expected);
    }

    public static <T> void matches(Collection<T> actual, Collection<T> expected, String... fieldsToIgnore) {
        assertThat(actual).usingRecursiveComparison().ignoringFields(fieldsToIgnore).isEqualTo(expected);
    }

    public static <T> T parseObject(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return readValue(result.getResponse().getContentAsString(), clazz);
    }

    public static <T> List<T> parseObjects(MvcResult result, Class<T[]> clazz) throws UnsupportedEncodingException {
        return (List<T>) readValues(result.getResponse().getContentAsString(), clazz);
    }
}
