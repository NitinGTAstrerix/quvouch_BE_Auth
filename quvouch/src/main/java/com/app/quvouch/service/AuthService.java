package com.app.quvouch.service;

import com.app.quvouch.dtos.UserDto;

public interface AuthService {
    public UserDto registerUser(UserDto userDto);
}
