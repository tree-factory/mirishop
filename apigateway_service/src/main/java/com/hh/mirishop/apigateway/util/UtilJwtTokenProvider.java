package com.hh.mirishop.apigateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class UtilJwtTokenProvider {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    public Long getMemberNumber(final String token) {
        return extractClaims(token).get("memberNumber", Long.class);
    }

    private Claims extractClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(generateHmacShaKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key generateHmacShaKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }
}