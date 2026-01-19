package com.app.quvouch.config;

public class AppConstant {

    public static final String[] publicUrls ={
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/api/v1/user",
            "/api/v1/auth/**"
    };

    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";
    public static final String CLIENT_ROLE = "CLIENT";
    public static final String SALE_REPRESENTATIVE = "SALE_REPRESENTATIVE";
}
