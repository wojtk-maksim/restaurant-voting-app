package ru.javaops.restaurantvoting.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

// https://sabljakovich.medium.com/adding-basic-auth-authorization-option-to-openapi-swagger-documentation-java-spring-95abbede27e9
@Configuration
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "JWT"
)
@OpenAPIDefinition(
        info = @Info(
                title = "REST API documentation",
                version = "1.0",
                description = """
                        Restaurant Voting application
                        <p><b>Test credentials:</b><br>
                        - user@yandex.ru / userPassword<br>
                        - admin@gmail.com / adminPassword<br>
                        - super.admin@gmail.com / superAdminPassword<br>
                        - deleted@gmail.com / deletedPassword</p>
                        - banned@gmail.com / bannedPassword</p>
                        """,
                contact = @Contact(name = "Maksim Wojtk", email = "wojtk.maksim@gmail.com")
        ),
        security = @SecurityRequirement(name = "JWT")
)
public class OpenApiConfig {
}
