package com.spring.jwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.jwt.utils.BaseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

    @Component
    public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override

        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException ex) throws IOException {

            BaseResponseDTO dto = BaseResponseDTO.builder()
                    .code("401")
                    .message(ex.getMessage())
                    .build();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            new ObjectMapper().writeValue(response.getWriter(), dto);
        }
    }

