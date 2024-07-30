package com.college.attendace.exceptions;

public class AttendaceDetailsNotFoundException extends RuntimeException{
    public AttendaceDetailsNotFoundException(){
        super("Attendance details not found");
    }
}
