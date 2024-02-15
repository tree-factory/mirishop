package com.hh.mirishop.apigateway.filter;

import com.hh.mirishop.apigateway.util.UtilJwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final UtilJwtTokenProvider jwtTokenProvider;
    private final RouteValidator routeValidator;

    public AuthenticationFilter(UtilJwtTokenProvider jwtTokenProvider, RouteValidator routeValidator) {
        super(Config.class);
        this.jwtTokenProvider = jwtTokenProvider;
        this.routeValidator = routeValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("헤더가 토큰을 가지고 있는지 검증");
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                ServerHttpRequest request = exchange.getRequest();
                ServerHttpRequest modifiedRequest = null;

                try {
                    if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                        throw new RuntimeException("Missing authorization header");
                    }

                    String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                    String token = extractTokenByHeader(authorizationHeader); // Remove "Bearer " prefix

                    if (!jwtTokenProvider.validateToken(token)) {
                        throw new RuntimeException("Invalid or expired JWT token");
                    }

                    Long extractMemberNumber = jwtTokenProvider.getMemberNumber(token);
                    URI newUri = addParam(request.getURI(), "member", extractMemberNumber);
                    modifiedRequest = exchange.getRequest().mutate().uri(newUri).build();

                } catch (Exception e) {
                    log.error(e.getMessage());
                    throw new RuntimeException("Missing authorization header");
                }
                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {

        // Configuration 추후 추가

    }

    private String extractTokenByHeader(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    private URI addParam(URI uri, String name, Long value) {
        return UriComponentsBuilder.fromUri(uri)
                .queryParam(name, value)
                .build(true)
                .toUri();
    }
}
