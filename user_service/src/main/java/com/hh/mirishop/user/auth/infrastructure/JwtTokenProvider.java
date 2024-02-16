package com.hh.mirishop.user.auth.infrastructure;

import com.hh.mirishop.user.auth.dto.TokenResponse;
import com.hh.mirishop.user.common.exception.ErrorCode;
import com.hh.mirishop.user.common.exception.JwtTokenException;
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

    public TokenResponse generateTokenResponse(Long memberNumber, String memberEmail) {
        Date now = new Date();
        Date accessTokenValidity = new Date(now.getTime() + validTime);
        Date refreshTokenValidity = new Date(now.getTime() + refreshTokenValidTime);

        String accessToken = createToken(memberNumber, memberEmail, accessTokenValidity);
        String refreshToken = createRefreshToken(memberEmail, refreshTokenValidity);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .expiresIn(accessTokenValidity.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public String createToken(Long memberNumber, String memberEmail, Date validity) {
        Claims claims = Jwts.claims();
        claims.put("memberNumber", memberNumber);
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
                throw new JwtTokenException(ErrorCode.INVALID_TOKEN);
            }
            return Jwts.parserBuilder()
                    .setSigningKey(generateHmacShaKey(secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("memberEmail", String.class);
        } catch (NumberFormatException e) {
            throw new JwtTokenException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String extractEmailFromRefreshToken(String token) {
        if (!validateToken(token)) {
            throw new JwtTokenException(ErrorCode.INVALID_TOKEN);
        }
        // 토큰에서 subject에 있는 email 반환
        return Jwts.parserBuilder()
                .setSigningKey(generateHmacShaKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            throw new JwtTokenException(ErrorCode.INVALID_TOKEN);
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