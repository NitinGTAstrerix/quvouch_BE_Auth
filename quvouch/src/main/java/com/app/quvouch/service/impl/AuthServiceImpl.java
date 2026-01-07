package com.app.quvouch.service.impl;

import com.app.quvouch.dtos.UserDto;
import com.app.quvouch.service.AuthService;
import com.app.quvouch.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    @Override
    public UserDto registerUser(UserDto userDto) {
        UserDto user = userService.createUser(userDto);
        return user;
    }
}
