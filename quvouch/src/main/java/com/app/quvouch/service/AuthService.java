package com.app.quvouch.service;

import com.app.quvouch.entity.User;
import com.app.quvouch.dtos.RegisterRequest;
import com.app.quvouch.dtos.UserDto;

public interface AuthService {
    public UserDto registerUser(RegisterRequest request);
    User CurrentUser();
}
