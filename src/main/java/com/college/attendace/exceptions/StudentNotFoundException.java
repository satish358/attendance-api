package com.college.attendace.exceptions;

public class StudentNotFoundException extends RuntimeException{
    public StudentNotFoundException(){
        super("Student not found");
    }
}
