package com.app.quvouch.dtos;


public record LoginRequest(
        String email,
        String password
) {
}