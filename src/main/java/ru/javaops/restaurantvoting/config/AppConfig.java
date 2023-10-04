package ru.javaops.restaurantvoting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import ru.javaops.restaurantvoting.service.CacheHelper;
import ru.javaops.restaurantvoting.service.UserService;
import ru.javaops.restaurantvoting.util.*;

@Configuration
@EnableCaching
@Slf4j
public class AppConfig {

    /*@Bean(initMethod = "start", destroyMethod = "stop")
    Server h2Server() throws SQLException {
        log.info("Start H2 TCP server");
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }*/

    @Autowired
    void configureAndStoreObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new Hibernate6Module());
        JsonUtil.setObjectMapper(objectMapper);
    }

    @Autowired
    void configureUtilClasses(MessageSourceAccessor messageSourceAccessor, UserService userService, CacheHelper cacheHelper,
                              @Value("${deadline.time}") String time, @Value("${deadline.timezone}") String timezone) {
        ValidationUtil.setMessageSource(messageSourceAccessor);
        UserUtil.setUserService(userService);
        RestaurantUtil.setCacheHelper(cacheHelper);
        DateTimeUtil.setDeadlineData(time, timezone);
    }

}
