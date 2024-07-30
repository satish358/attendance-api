package com.college.attendace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class ViewAttendanceDTO {
    private String status;
    private LocalDate date;
    private String prn;
    private String studentName;
}
