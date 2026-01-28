package com.app.quvouch.entity;

import org.springframework.http.HttpStatus;

import java.time.LocalTime;

public record ErrorMessage(
        String msg,
        HttpStatus status,
        LocalTime time
) {
}