package com.hh.mirishop.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.tokenValid-time}")
    private long validTime;

    @Value("${security.jwt.refreshtokenValid-time}")
    private long refreshTokenValidTime;

    public String createToken(String memberEmail) {
        Claims claims = Jwts.claims();
        claims.put("memberEmail", memberEmail);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validTime);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(generateHmacShaKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String key) {
        Claims claims = Jwts.claims();

        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(generateHmacShaKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key generateHmacShaKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long extractId(String token) {
        try {
            if (!validateToken(token)) {
                throw new RuntimeException("토큰값이 유효하지 않습니다.");
            }
            String subject = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return Long.parseLong(subject);
        } catch (NumberFormatException e) {
            throw new RuntimeException("토큰값이 유효하지 않습니다.");
        }
    }

    private boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            throw new RuntimeException("토큰값이 유효하지 않습니다.");
        }
    }
}