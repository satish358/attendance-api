package com.college.attendace.exceptions;

public class AttendanceTimeEndedException extends RuntimeException{
    public AttendanceTimeEndedException() {
        super("Attendance Time Expired");
    }
}
