package ru.javaops.restaurantvoting.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.javaops.restaurantvoting.error.authentication.ExpiredAuthenticationException;

import java.io.IOException;

import static ru.javaops.restaurantvoting.config.SecurityConfig.EXCEPTION_RESOLVER;
import static ru.javaops.restaurantvoting.util.JwtUtil.getUserAuthenticationToken;
import static ru.javaops.restaurantvoting.util.UserUtil.checkBannedOrDeleted;
import static ru.javaops.restaurantvoting.web.UrlData.API_PATH;
import static ru.javaops.restaurantvoting.web.public_access.ProfileController.PROFILE_URL;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final MessageSourceAccessor messageSourceAccessor;

    @Override
    public void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String jwt = authHeader.substring(7);
                AuthToken authUser = getUserAuthenticationToken(jwt);
                checkBannedOrDeleted(authUser);
                SecurityContextHolder.getContext().setAuthentication(authUser);
                filterChain.doFilter(request, response);
            } catch (ExpiredJwtException ex) {
                EXCEPTION_RESOLVER.resolveException(request, response, null, new ExpiredAuthenticationException(
                        messageSourceAccessor.getMessage("error.authExpired")
                ));
            } catch (JwtException ex) {
                EXCEPTION_RESOLVER.resolveException(request, response, null, new IllegalArgumentException(ex.getMessage()));
            } catch (Exception ex) {
                EXCEPTION_RESOLVER.resolveException(request, response, null, ex);
            }
        } else {
            filterChain.doFilter(request, response);
        }

    }

    // https://stackoverflow.com/questions/36795894/how-to-apply-spring-security-filter-only-on-secured-endpoints
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return !uri.startsWith(API_PATH) ||
                (uri.startsWith(PROFILE_URL) && "POST".equals(request.getMethod()));
    }

}
