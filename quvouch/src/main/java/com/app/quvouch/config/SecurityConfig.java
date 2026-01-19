package com.app.quvouch.config;

import com.app.quvouch.Models.ErrorMessage;
import com.app.quvouch.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authRequest -> authRequest
                        .requestMatchers(AppConstant.publicUrls)
                        .permitAll()
                        .anyRequest().authenticated())
//                .httpBasic(Customizer.withDefaults());

                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                    authException.printStackTrace();
                    response.setStatus(401);
                    response.setContentType("application/json");
                    String msg = "Unauthorized Access " + authException.getMessage();
                    String error = (String) request.getAttribute("error");
                    if (error !=null)
                    {
                        msg = error;
                    }
                    Map<String, String> errorMessage = Map.of("message", msg, "status", String.valueOf(401));
                    var objectMapper = new ObjectMapper();
                    response.getWriter().write(objectMapper.writeValueAsString(errorMessage));
                })
                                .accessDeniedHandler(((request, response, accessDeniedException) ->
                                {
                                    response.setStatus(403);
                                    response.setContentType("application/json");
                                    String msg = accessDeniedException.getMessage();
                                    Object error = request.getAttribute("error");
                                    if (error != null)
                                    {
                                        msg = (String) error;
                                    }
                                    Map<String, String> errorMessage = Map.of("message", msg, "status", String.valueOf(403));
                                    var objectMapper = new ObjectMapper();
                                    response.getWriter().write(objectMapper.writeValueAsString(errorMessage));

                                }))
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
