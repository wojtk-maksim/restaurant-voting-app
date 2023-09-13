package ru.javaops.restaurantvoting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.javaops.restaurantvoting.service.RestaurantService;
import ru.javaops.restaurantvoting.service.UserService;
import ru.javaops.restaurantvoting.util.JsonUtil;
import ru.javaops.restaurantvoting.util.RestaurantUtil;
import ru.javaops.restaurantvoting.util.UserUtil;

import java.sql.SQLException;

@Configuration
@EnableCaching
@Slf4j
public class AppConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    Server h2Server() throws SQLException {
        log.info("Start H2 TCP server");
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

    @Autowired
    void configureAndStoreObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new Hibernate6Module());
        JsonUtil.setObjectMapper(objectMapper);
    }

    @Autowired
    void configureUtil(RestaurantService restaurantService, UserService userService) {
        RestaurantUtil.setRestaurantService(restaurantService);
        UserUtil.setUserService(userService);
    }

}
