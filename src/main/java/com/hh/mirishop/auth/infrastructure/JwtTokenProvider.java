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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";

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
                .grantType(BEARER_TYPE)
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

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            throw new RuntimeException("토큰값이 유효하지 않습니다.");
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}