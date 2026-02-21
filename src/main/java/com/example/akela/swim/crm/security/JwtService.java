package com.example.akela.swim.crm.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final JwtProperties props;
    private final SecretKey key;

    public JwtService(JwtProperties props) {
        this.props = props;

        SecretKey k;
        try {
            byte[] decoded = Decoders.BASE64.decode(props.secret());
            k = Keys.hmacShaKeyFor(decoded);
        } catch (Exception ex) {
            k = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
        }
        this.key = k;
    }

    public long expirationSeconds() {
        return props.expirationSeconds();
    }

    public String generateToken(AklUserPrincipal principal) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationSeconds());

        Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("role", principal.getRole());

        if (principal.getCoachId() != null) {
            claims.put("coachId", principal.getCoachId());
        }


        return Jwts.builder()
                .issuer(props.issuer())
                .subject(principal.getEmail())
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .requireIssuer(props.issuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isValid(String token) {
        try {
            Claims c = parseClaims(token);
            return c.getExpiration() != null && c.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
