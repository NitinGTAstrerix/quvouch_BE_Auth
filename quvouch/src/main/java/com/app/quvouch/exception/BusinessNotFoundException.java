package com.app.quvouch.exception;

public class BusinessNotFoundException extends RuntimeException{

    public BusinessNotFoundException(String msg)
    {
        super(msg);
    }
    BusinessNotFoundException()
    {
        super("Business Is Not Found");
    }
}
