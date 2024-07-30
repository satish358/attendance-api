package com.college.attendace.exceptions;

public class ConfirmPasswordNotMatchedException extends RuntimeException{
    public ConfirmPasswordNotMatchedException(String message){
        super(message);
    }
    public ConfirmPasswordNotMatchedException() {
        super("Password and confirm password not matched");
    }
}
