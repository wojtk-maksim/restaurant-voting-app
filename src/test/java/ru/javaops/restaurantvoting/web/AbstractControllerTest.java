package ru.javaops.restaurantvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.javaops.restaurantvoting.AbstractTest;

@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractTest {

    @Autowired
    protected MockMvc mockMvc;
}
