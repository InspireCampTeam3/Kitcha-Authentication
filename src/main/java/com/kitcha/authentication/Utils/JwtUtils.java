package com.kitcha.authentication.Utils;

import com.kitcha.authentication.dto.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
public class JwtUtils {
    private final SecretKey hmacKey;
    private final Long expirationTime;

    public JwtUtils(Environment env) {
        this.hmacKey = Keys.hmacShaKeyFor(Objects.requireNonNull(env.getProperty("token.secret")).getBytes());
        this.expirationTime = Long.parseLong(Objects.requireNonNull(env.getProperty("token.expiration-time")));
    }

    public String generateToken(CustomUserDetails customUserDetails) {
        Date now = new Date();

        String jwtToken = Jwts.builder()
                .claim("nickname", customUserDetails.getNickname())
                .claim("role", customUserDetails.getRole())
                .subject(customUserDetails.getUsername())
                .id(String.valueOf(System.currentTimeMillis()))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + this.expirationTime))
                .signWith(this.hmacKey, Jwts.SIG.HS256)
                .compact();
        // TODO : delete log
        log.debug(jwtToken);

        return jwtToken;
    }
}
