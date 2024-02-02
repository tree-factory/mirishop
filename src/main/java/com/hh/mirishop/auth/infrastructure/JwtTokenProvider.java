package com.hh.mirishop.auth.infrastructure;

import com.hh.mirishop.auth.dto.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

    public TokenResponse generateTokenResponse(String memberEmail) {
        Date now = new Date();
        Date accessTokenValidity = new Date(now.getTime() + validTime);
        Date refreshTokenValidity = new Date(now.getTime() + refreshTokenValidTime);

        String accessToken = createToken(memberEmail, accessTokenValidity);
        String refreshToken = createRefreshToken(memberEmail, refreshTokenValidity);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .expiresIn(accessTokenValidity.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public String createToken(String memberEmail, Date validity) {
        Claims claims = Jwts.claims();
        claims.put("memberEmail", memberEmail);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(generateHmacShaKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String memberEmail, Date validity) {
        Claims claims = Jwts.claims().setSubject(memberEmail);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(generateHmacShaKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key generateHmacShaKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmailFromToken(String token) {
        try {
            if (!validateToken(token)) {
                throw new RuntimeException("토큰값이 유효하지 않습니다.");
            }
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("memberEmail", String.class);
        } catch (NumberFormatException e) {
            throw new RuntimeException("토큰값이 유효하지 않습니다.");
        }
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            throw new RuntimeException("토큰값이 유효하지 않습니다.");
        }
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}