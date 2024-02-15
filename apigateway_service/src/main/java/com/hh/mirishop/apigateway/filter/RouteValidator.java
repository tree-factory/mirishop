package com.hh.mirishop.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/api/v1/members/register",
            "/api/v1/members/email-authentication",
            "/api/v1/members/email-verification",
            "/api/v1/members/upload/image",
            "/api/v1/login",
            "/api/v1/refreshToken"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
