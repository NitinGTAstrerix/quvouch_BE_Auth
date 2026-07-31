package com.spring.jwt.controller;

import com.spring.jwt.jwt.ActiveSessionService;
import com.spring.jwt.jwt.JwtConfig;
import com.spring.jwt.jwt.JwtService;
import com.spring.jwt.utils.BaseResponseDTO;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtConfig jwtConfig;
    private final JwtService jwtService;
    private final ActiveSessionService activeSessionService;

    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    @PostMapping("/logout")
    public ResponseEntity<BaseResponseDTO> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        log.info("Processing logout request");

        try {
            String accessToken = null;

            // ==========================================
            // 1. Get access token from Authorization header
            // ==========================================
            String authHeader = request.getHeader(jwtConfig.getHeader());

            if (authHeader != null &&
                    authHeader.startsWith(jwtConfig.getPrefix() + " ")) {

                accessToken = authHeader.substring(
                        (jwtConfig.getPrefix() + " ").length()
                );
            }

            // ==========================================
            // 2. If header doesn't contain token,
            //    check access_token cookie
            // ==========================================
            if (accessToken == null && request.getCookies() != null) {

                for (Cookie cookie : request.getCookies()) {

                    if (ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                        accessToken = cookie.getValue();
                        break;
                    }
                }
            }

            // ==========================================
            // 3. Blacklist access token + remove session
            // ==========================================
            if (accessToken != null && !accessToken.isBlank()) {

                try {

                    Claims claims = jwtService.extractClaims(accessToken);

                    String username = claims.getSubject();

                    // Blacklist token
                    jwtService.blacklistToken(accessToken);

                    // Remove active session
                    if (username != null && !username.isBlank()) {
                        activeSessionService.removeSession(username);

                        log.info(
                                "Active session removed for user: {}",
                                username
                        );
                    }

                } catch (Exception e) {

                    // Logout should still continue even if
                    // token is already expired/invalid.
                    log.warn(
                            "Unable to invalidate access token during logout: {}",
                            e.getMessage()
                    );
                }
            }

            // ==========================================
            // 4. Delete access_token cookie
            // ==========================================
            Cookie accessCookie =
                    new Cookie(ACCESS_TOKEN_COOKIE_NAME, "");

            accessCookie.setHttpOnly(false);
            accessCookie.setSecure(false); // localhost HTTP
            accessCookie.setPath("/");
            accessCookie.setMaxAge(0);

            response.addCookie(accessCookie);

            // ==========================================
            // 5. Delete refresh_token cookie
            // ==========================================
            Cookie refreshCookie =
                    new Cookie(REFRESH_TOKEN_COOKIE_NAME, "");

            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(false); // localhost HTTP
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(0);
            refreshCookie.setAttribute("SameSite", "Strict");

            response.addCookie(refreshCookie);

            // ==========================================
            // 6. Clear Spring Security context
            // ==========================================
            SecurityContextHolder.clearContext();

            // ==========================================
            // 7. Response
            // ==========================================
            BaseResponseDTO responseDTO = new BaseResponseDTO();

            responseDTO.setCode(
                    String.valueOf(HttpStatus.OK.value())
            );

            responseDTO.setMessage("Logout successful");

            log.info("Logout successful");

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {

            log.error("Logout failed", e);

            BaseResponseDTO responseDTO = new BaseResponseDTO();

            responseDTO.setCode(
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
            );

            responseDTO.setMessage("Logout failed");

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseDTO);
        }
    }
    @GetMapping("/check-cookies")
    public ResponseEntity<Map<String, Object>> checkCookies(HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            response.put("cookieCount", cookies.length);

            Map<String, String> cookieDetails =
                    Arrays.stream(cookies)
                            .collect(Collectors.toMap(
                                    Cookie::getName,
                                    cookie -> {
                                        String value = cookie.getValue();
                                        if (value.length() > 10) {
                                            return value.substring(0, 5)
                                                    + "..."
                                                    + value.substring(value.length() - 5);
                                        }
                                        return value;
                                    }));

            response.put("cookies", cookieDetails);

            response.put(
                    "hasAccessToken",
                    Arrays.stream(cookies)
                            .anyMatch(c -> ACCESS_TOKEN_COOKIE_NAME.equals(c.getName()))
            );

            response.put(
                    "hasRefreshToken",
                    Arrays.stream(cookies)
                            .anyMatch(c -> REFRESH_TOKEN_COOKIE_NAME.equals(c.getName()))
            );

        } else {

            response.put("cookieCount", 0);
            response.put("cookies", Map.of());
            response.put("hasAccessToken", false);
            response.put("hasRefreshToken", false);
        }

        Map<String, String> headers = new HashMap<>();

        request.getHeaderNames().asIterator().forEachRemaining(
                name -> headers.put(name, request.getHeader(name))
        );

        response.put("headers", headers);

        return ResponseEntity.ok(response);
    }
}