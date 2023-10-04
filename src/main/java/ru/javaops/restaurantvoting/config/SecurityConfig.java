package ru.javaops.restaurantvoting.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static ru.javaops.restaurantvoting.config.Access.ADMIN_ACCESS;
import static ru.javaops.restaurantvoting.config.Access.SUPER_ADMIN_ACCESS;
import static ru.javaops.restaurantvoting.web.UrlData.ADMIN_PATH;
import static ru.javaops.restaurantvoting.web.UrlData.API_PATH;
import static ru.javaops.restaurantvoting.web.public_access.ProfileController.PROFILE_URL;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public static HandlerExceptionResolver EXCEPTION_RESOLVER;

    @Autowired
    public SecurityConfig(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        SecurityConfig.EXCEPTION_RESOLVER = exceptionResolver;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PASSWORD_ENCODER;
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) ->
                EXCEPTION_RESOLVER.resolveException(request, response, null, authException);
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return (request, response, authException) ->
                EXCEPTION_RESOLVER.resolveException(request, response, null, authException);
    }

    @Bean
    @Autowired
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtFilter jwtFilter) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, PROFILE_URL + "/register").anonymous()
                        .requestMatchers(HttpMethod.POST, PROFILE_URL + "/auth").anonymous()
                        .requestMatchers(HttpMethod.DELETE, API_PATH + "/**/hard-delete").hasAuthority(SUPER_ADMIN_ACCESS.getAuthority())
                        .requestMatchers(API_PATH + ADMIN_PATH + "/**").hasAnyAuthority(ADMIN_ACCESS.getAuthority(), SUPER_ADMIN_ACCESS.getAuthority())
                        .requestMatchers(API_PATH + "/problems/**").permitAll()
                        .requestMatchers(API_PATH + "/**").authenticated()
                        .requestMatchers("/error", "/swagger", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                );

        return httpSecurity.build();
    }

}
