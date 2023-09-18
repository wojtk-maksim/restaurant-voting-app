package ru.javaops.restaurantvoting.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.web.AuthUser;

import static ru.javaops.restaurantvoting.config.Access.ADMIN_ACCESS;
import static ru.javaops.restaurantvoting.config.Access.SUPER_ADMIN_ACCESS;
import static ru.javaops.restaurantvoting.util.ValidationUtil.USER;
import static ru.javaops.restaurantvoting.web.UrlData.ADMIN;
import static ru.javaops.restaurantvoting.web.UrlData.API;
import static ru.javaops.restaurantvoting.web.public_access.AccountController.ACCOUNT_URL;

@Configuration
@EnableWebSecurity
@Slf4j
@AllArgsConstructor
public class SecurityConfig {

    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private UserRepository userRepository;

    private AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    PasswordEncoder passwordEncoder() {
        return PASSWORD_ENCODER;
    }

    @Bean
    UserDetailsService userDetailsService(HttpServletRequest request, HttpServletResponse response) {
        return email -> {
            log.debug("authenticating {}", email);
            email = email.toLowerCase();
            User user = userRepository.getByEmail(email);
            if (user == null || !user.isEnabled() || user.isDeleted()) {
                throw new UsernameNotFoundException(USER + " " + email + " not found");
            }
            return new AuthUser(user);
        };
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**");
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher(API + "/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(API + ADMIN + "/**").hasAnyAuthority(ADMIN_ACCESS.getAuthority(), SUPER_ADMIN_ACCESS.getAuthority())
                        .requestMatchers(HttpMethod.POST, ACCOUNT_URL).anonymous()
                        .requestMatchers(API + "/**").authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(authenticationEntryPoint));

        return httpSecurity.build();
    }

}
