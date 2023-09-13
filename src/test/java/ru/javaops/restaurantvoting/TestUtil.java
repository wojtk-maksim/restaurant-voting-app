package ru.javaops.restaurantvoting;

import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javaops.restaurantvoting.util.JsonUtil.readValue;
import static ru.javaops.restaurantvoting.util.JsonUtil.readValues;

public class TestUtil {

    public static final Long NOT_FOUND = Long.MAX_VALUE;

    public static <T> void matches(T actual, T expected, List<String> fieldsToIgnore) {
        assertThat(actual).usingRecursiveComparison().ignoringFields(fieldsToIgnore.toArray(new String[0])).isEqualTo(expected);
    }

    public static <T> void matches(Collection<T> actual, Collection<T> expected, List<String> fieldsToIgnore) {
        assertThat(actual).usingRecursiveComparison().ignoringFields(fieldsToIgnore.toArray(new String[0])).isEqualTo(expected);
    }

    public static <T> T parseObject(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return readValue(result.getResponse().getContentAsString(), clazz);
    }

    public static <T> List<T> parseObjects(MvcResult result, Class<T[]> clazz) throws UnsupportedEncodingException {
        return (List<T>) readValues(result.getResponse().getContentAsString(), clazz);
    }

    public static class Matcher<T> {

        private final List<String> fieldsToIgnore;

        public Matcher(List<String> fieldsToIgnore) {
            this.fieldsToIgnore = fieldsToIgnore;
        }

        public void matches(T actual, T expected, String... additionalFieldsToIgnore) {
            List<String> fieldsToIgnore = getAllFieldsToIgnore(additionalFieldsToIgnore);
            assertThat(actual).usingRecursiveComparison().ignoringFields(fieldsToIgnore.toArray(new String[0])).isEqualTo(expected);
        }

        public void matches(Collection<T> actual, Collection<T> expected, String... additionalFieldsToIgnore) {
            List<String> fieldsToIgnore = getAllFieldsToIgnore(additionalFieldsToIgnore);
            assertThat(actual).usingRecursiveComparison().ignoringFields(fieldsToIgnore.toArray(new String[0])).isEqualTo(expected);
        }

        public void matches(MvcResult result, T expected, Class<T> clazz, String... additionalFieldsToIgnore) throws UnsupportedEncodingException {
            List<String> fieldsToIgnore = getAllFieldsToIgnore(additionalFieldsToIgnore);
            T actual = parseObject(result, clazz);
            assertThat(actual).usingRecursiveComparison().ignoringFields(fieldsToIgnore.toArray(new String[0])).isEqualTo(expected);
        }

        public void matches(MvcResult result, Collection<T> expected, Class<T[]> clazz, String... additionalFieldsToIgnore) throws UnsupportedEncodingException {
            List<String> fieldsToIgnore = getAllFieldsToIgnore(additionalFieldsToIgnore);
            List<T> actual = parseObjects(result, clazz);
            assertThat(actual).usingRecursiveComparison().ignoringFields(fieldsToIgnore.toArray(new String[0])).isEqualTo(expected);
        }

        private T parseObject(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
            return readValue(result.getResponse().getContentAsString(), clazz);
        }

        private List<T> parseObjects(MvcResult result, Class<T[]> clazz) throws UnsupportedEncodingException {
            return (List<T>) readValues(result.getResponse().getContentAsString(), clazz);
        }

        private List<String> getFieldsToIgnore() {
            return new ArrayList<>(fieldsToIgnore);
        }

        private List<String> getAllFieldsToIgnore(String... additionalFieldsToIgnore) {
            if (additionalFieldsToIgnore != null) {
                return Stream.concat(
                        fieldsToIgnore.stream(),
                        Arrays.stream(additionalFieldsToIgnore)
                ).toList();
            }
            return getFieldsToIgnore();
        }

    }

}
