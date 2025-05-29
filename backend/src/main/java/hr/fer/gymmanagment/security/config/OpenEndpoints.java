package hr.fer.gymmanagment.security.config;

import static hr.fer.gymmanagment.common.Constants.Api.*;

public enum OpenEndpoints {

    GET (
            "/swagger-ui/**",
            "/favicon.ico",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/actuator/**",
            "/h2-console/**"
    ),

    POST (
            V1 + USERS + REGISTRATION,
            V1 + USERS + LOGIN,
            "/h2-console/**"
    );

    private final String[] endpoints;

    OpenEndpoints(String... endpoints) {
        this.endpoints = endpoints;
    }

    public String[] getEndpoints() {
        return endpoints;
    }
}
