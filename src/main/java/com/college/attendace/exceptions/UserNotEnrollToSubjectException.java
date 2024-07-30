package com.college.attendace.exceptions;

public class UserNotEnrollToSubjectException extends RuntimeException{
    public UserNotEnrollToSubjectException() {
        super("User not enroll to subject.");
    }
}
