package com.app.quvouch.exception;


public class EmailAlreadyExit extends RuntimeException{
    public EmailAlreadyExit(String msg)
    {
        super(msg);
    }
    public EmailAlreadyExit()
    {
        super("Email is already Exits in Our Database");
    }
}
