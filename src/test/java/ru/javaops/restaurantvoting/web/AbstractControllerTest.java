package ru.javaops.restaurantvoting.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.util.JsonUtil;

@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractTest {

    @Autowired
    protected MockMvc mockMvc;

    protected ResultActions perform(RequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder);
    }

    // https://stackoverflow.com/a/55064740/22653131
    private final JacksonAnnotationIntrospector annotationIntrospector = new JacksonAnnotationIntrospector() {
        @Override
        public JsonProperty.Access findPropertyAccess(Annotated m) {
            return JsonProperty.Access.AUTO;
        }
    };

    @BeforeEach
    protected void disableJacksonAccessAnnotations() {
        JsonUtil.mapper.setAnnotationIntrospector(annotationIntrospector);
    }

}
