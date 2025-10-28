package com.apikeygen.apikeygenerator.security;

import com.apikeygen.apikeygenerator.dto.LogRequestDTO;
import com.apikeygen.apikeygenerator.dto.LogResponseDTO;
import com.apikeygen.apikeygenerator.dto.RegRequestDTO;
import com.apikeygen.apikeygenerator.model.User;
import com.apikeygen.apikeygenerator.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

@Service
public class TokenService {
    @Value("${api.security.jwt.secret-key}")
    private String secretKey;
    private static final long T0KEN_VALIDITY_HR = 24;

    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant exp = now.plus(T0KEN_VALIDITY_HR, ChronoUnit.HOURS);
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(getSigingKey())
                .compact();
    }

    private SecretKey getSigingKey() {
        byte[] keybytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keybytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
