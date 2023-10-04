package ru.javaops.restaurantvoting.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.javaops.restaurantvoting.config.AuthToken;
import ru.javaops.restaurantvoting.model.Role;
import ru.javaops.restaurantvoting.model.User;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.util.Objects.requireNonNull;

@Component
public class JwtUtil {

    private static Key secretKey;

    private static Duration lifetime;

    private static JwtParser jwtParser;

    // https://stackoverflow.com/a/55620766
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.lifetime}") Duration lifetime) {
        JwtUtil.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        JwtUtil.lifetime = lifetime;
        jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
    }

    public static String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", user.getId());
        claims.put("name", user.getName());
        claims.put("email", user.getName());
        claims.put("role", user.getRole());

        Date issued = new Date();
        Date expiration = new Date(issued.getTime() + lifetime.toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(issued)
                .setExpiration(expiration)
                .signWith(secretKey, HS256)
                .compact();
    }

    public static AuthToken getUserAuthenticationToken(String jwt) {
        Claims jwtBody = jwtParser.parseClaimsJws(jwt).getBody();
        return new AuthToken(
                requireNonNull(Long.valueOf(String.valueOf(jwtBody.get("id")))),
                requireNonNull(String.valueOf(jwtBody.get("name"))),
                requireNonNull(jwtBody.getSubject()),
                requireNonNull(Role.valueOf(String.valueOf(jwtBody.get("role"))))
        );
    }

}
