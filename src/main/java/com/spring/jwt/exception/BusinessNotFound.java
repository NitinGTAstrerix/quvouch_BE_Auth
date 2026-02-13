package com.spring.jwt.exception;

import org.springframework.http.HttpStatus;

public class BusinessNotFound extends RuntimeException{
    public BusinessNotFound(String msg){super(msg);}
}
