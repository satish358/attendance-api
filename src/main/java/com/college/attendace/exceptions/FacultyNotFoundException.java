package com.college.attendace.exceptions;

public class FacultyNotFoundException extends RuntimeException{
    public FacultyNotFoundException() {
        super("Faculty not found.");
    }
}
