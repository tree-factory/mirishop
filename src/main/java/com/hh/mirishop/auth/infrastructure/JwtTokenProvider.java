package com.hh.mirishop.auth.infrastructure;

import com.hh.mirishop.auth.dto.TokenResponse;
import com.hh.mirishop.common.exception.ErrorCode;
import com.hh.mirishop.common.exception.JwtTokenException;
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
                throw new JwtTokenException(ErrorCode.INVALID_TOKEN);
            }
            // 토큰에서 subject에 있는 email 반환
            return Jwts.parserBuilder()
                    .setSigningKey(generateHmacShaKey(secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtTokenException(ErrorCode.INVALID_TOKEN);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            throw new JwtTokenException(ErrorCode.INVALID_TOKEN);
        }
    }
}