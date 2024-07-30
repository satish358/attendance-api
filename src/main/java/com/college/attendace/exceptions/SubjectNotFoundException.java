package com.college.attendace.exceptions;


public class SubjectNotFoundException extends RuntimeException {
    public SubjectNotFoundException() {
        super("Subject not found");
    }
}
