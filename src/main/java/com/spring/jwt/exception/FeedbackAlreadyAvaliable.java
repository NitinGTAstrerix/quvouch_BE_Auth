package com.spring.jwt.exception;

public class FeedbackAlreadyAvaliable extends RuntimeException{
    public FeedbackAlreadyAvaliable(String message){
        super(message);
    }
}
