package com.spring.jwt.config;

import com.spring.jwt.config.filter.*;
import com.spring.jwt.exception.CustomAccessDeniedHandler;
import com.spring.jwt.exception.CustomAuthenticationEntryPoint;
import com.spring.jwt.jwt.JwtConfig;
import com.spring.jwt.jwt.JwtService;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.security.UserDetailsServiceCustom;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.ForwardedHeaderFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableScheduling
@EnableMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
@Slf4j
public class AppConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private JwtService jwtService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private SecurityHeadersFilter securityHeadersFilter;

    @Autowired
    private XssFilter xssFilter;

    @Autowired
    private SqlInjectionFilter sqlInjectionFilter;

    @Autowired
    private RateLimitingFilter rateLimitingFilter;

    @Autowired
    private com.spring.jwt.jwt.ActiveSessionService activeSessionService;

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Value("${app.url.frontend:http://localhost:5173}")
    private String frontendUrl;

    @Value("#{'${app.cors.allowed-origins:http://localhost:5173,http://localhost:3000,http://localhost:8080,http://localhost:5173/,http://localhost:8091/,http://localhost:8085/}'.split(',')}")
    private List<String> allowedOrigins;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsServiceCustom userDetailsService() {
        return new UserDetailsServiceCustom(userRepository);
    }

    @Bean
    public JwtRefreshTokenFilter jwtRefreshTokenFilter(
            AuthenticationManager authenticationManager,
            JwtConfig jwtConfig,
            JwtService jwtService,
            UserDetailsServiceCustom userDetailsService,
            com.spring.jwt.jwt.ActiveSessionService activeSessionService) {
        return new JwtRefreshTokenFilter(authenticationManager, jwtConfig, jwtService, userDetailsService, activeSessionService);
    }

    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("Configuring security filter chain");

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .ignoringRequestMatchers(
                "/api/**",
                "/user/**",

                jwtConfig.getUrl(),
                jwtConfig.getRefreshUrl()
            )

        );

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(new CustomAccessDeniedHandler())
        );

        http.headers(headers -> headers
                .xssProtection(xss -> xss
                    .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                .frameOptions(frame -> frame.deny())
                .referrerPolicy(referrer -> referrer
                    .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                .permissionsPolicy(permissions -> permissions
                    .policy("camera=(), microphone=(), geolocation=()"))
            );

        log.debug("Configuring URL-based security rules");
        http.authorizeHttpRequests(authorize -> authorize

                // CORS preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Public APIs
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/feedback/**").permitAll()
                .requestMatchers("/api/v1/users/register").permitAll()
                .requestMatchers("/api/v1/users/password/**").permitAll()
                .requestMatchers("/api/v1/exam/**").permitAll()
                .requestMatchers("/scan/qr/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/user/**").permitAll()

                // Swagger
                .requestMatchers(
                        "/v2/api-docs/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html"
                ).permitAll()

                // Admin
                .requestMatchers("/api/v1/admin/**")
                .hasAuthority("ADMIN")

                // Review APIs
                .requestMatchers("/api/v1/qr/reviews")
                .hasAuthority("ADMIN")

                .requestMatchers("/api/v1/qr/reviews/business/**")
                .hasAnyAuthority("ADMIN", "SALE_REPRESENTATIVE", "CLIENT")

                .requestMatchers(HttpMethod.POST, "/api/v1/qr/*/rate")
                .permitAll()

                // Everything else requires login
                .anyRequest().authenticated()
        );
        // Create a request matcher for public URLs
        org.springframework.security.web.util.matcher.RequestMatcher publicUrls =
            new org.springframework.security.web.util.matcher.OrRequestMatcher(
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/auth/**"),
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/public/**"),
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/v1/users/register"),
                    new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/v1/users/password/**"),
                    new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/v1/qr/reviews/**"),
                    new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/v1/qr/*/rate"),
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/v1/feedback",HttpMethod.POST.name()),
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/scan/qr/**"),
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/v2/api-docs/**"),
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/v3/api-docs/**"),
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/swagger-resources/**"),
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/swagger-ui/**"),
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/configuration/**"),
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/webjars/**"),
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/swagger-ui.html"),
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/user/**"),
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher(jwtConfig.getUrl()),
                new org.springframework.security.web.util.matcher.AntPathRequestMatcher(jwtConfig.getRefreshUrl())
            );

        log.debug("Configuring security filters");
        JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter = new JwtUsernamePasswordAuthenticationFilter(authenticationManager(http), jwtConfig, jwtService, userRepository, activeSessionService);
        JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter = new JwtTokenAuthenticationFilter(jwtConfig, jwtService, userDetailsService(), publicUrls, activeSessionService);
        JwtRefreshTokenFilter jwtRefreshTokenFilter = new JwtRefreshTokenFilter(authenticationManager(http), jwtConfig, jwtService, userDetailsService(), activeSessionService);

        http.addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtRefreshTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(xssFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(sqlInjectionFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(securityHeadersFilter, UsernamePasswordAuthenticationFilter.class);

        http.authenticationProvider(customAuthenticationProvider);

        log.debug("Security configuration completed");
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(allowedOrigins);

        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        configuration.setAllowCredentials(true);

        configuration.setExposedHeaders(List.of(
                "Authorization"
        ));

        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
        return builder.build();
    }
}