package com.app.quvouch.service.impl;

import com.app.quvouch.Models.User;
import com.app.quvouch.dtos.RegisterRequest;
import com.app.quvouch.dtos.UserDto;
import com.app.quvouch.exception.UserNotFoundWithNameException;
import com.app.quvouch.repository.UserRepository;
import com.app.quvouch.service.AuthService;
import com.app.quvouch.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public UserDto registerUser(RegisterRequest request) {
        UserDto user = userService.createUser(request);
        return user;
    }

    @Override
    public User CurrentUser() {
        Object principal = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        if (principal instanceof String email)
        {
            return userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundWithNameException("User is not Found"));
        }
        throw new RuntimeException("Unauthorized");
    }
}
