package ru.javaops.restaurantvoting;

import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javaops.restaurantvoting.util.JsonUtil.readValue;
import static ru.javaops.restaurantvoting.util.JsonUtil.readValues;

public class Matcher<T> {

    private final Class<T> clazz;

    private final String[] fieldsToIgnore;

    public Matcher(Class<T> clazz, String... fieldsToIgnore) {
        this.fieldsToIgnore = fieldsToIgnore;
        this.clazz = clazz;
    }

    public void matches(T actual, T expected, String... additionalFieldsToIgnore) {
        makeAssertion(actual, expected, getAllFieldsToIgnore(additionalFieldsToIgnore));
    }

    public void matches(Collection<T> actual, Collection<T> expected, String... additionalFieldsToIgnore) {
        makeAssertion(actual, expected, getAllFieldsToIgnore(additionalFieldsToIgnore));
    }

    public void matches(MvcResult result, T expected, String... additionalFieldsToIgnore) throws UnsupportedEncodingException {
        T actual = parseObject(result, clazz);
        makeAssertion(actual, expected, getAllFieldsToIgnore(additionalFieldsToIgnore));
    }

    @SuppressWarnings(value = "all")
    public void matches(MvcResult result, Collection<T> expected, String... additionalFieldsToIgnore) throws UnsupportedEncodingException {
        List<T> actual = parseCollectionOfObjects(result, (Class<T[]>) clazz.arrayType());
        makeAssertion(actual, expected, getAllFieldsToIgnore(additionalFieldsToIgnore));
    }

    private void makeAssertion(Object actual, Object expected, String[] fieldsToIgnore) {
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(fieldsToIgnore)
                .isEqualTo(expected);
    }

    private T parseObject(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return readValue(result.getResponse().getContentAsString(), clazz);
    }

    private List<T> parseCollectionOfObjects(MvcResult result, Class<T[]> clazz) throws UnsupportedEncodingException {
        return (List<T>) readValues(result.getResponse().getContentAsString(), clazz);
    }

    private String[] getAllFieldsToIgnore(String... additionalFieldsToIgnore) {
        if (additionalFieldsToIgnore != null) {
            return Stream.concat(
                            Arrays.stream(fieldsToIgnore),
                            Arrays.stream(additionalFieldsToIgnore))
                    .toArray(String[]::new);
        }
        return fieldsToIgnore;
    }

}
