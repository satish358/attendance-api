package com.college.attendace.exceptions;

public class UserAlreadyPresentException extends RuntimeException{
    public UserAlreadyPresentException(String message){
        super(message);
    }
    public UserAlreadyPresentException() {
        super("User Already Exists");
    }
}
