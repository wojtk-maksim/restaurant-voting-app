package ru.javaops.restaurantvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.util.JsonUtil;

@AutoConfigureMockMvc
//@ContextConfiguration(classes = AppConfig.class)
public abstract class AbstractControllerTest extends AbstractTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JsonUtil jsonUtil;
}
