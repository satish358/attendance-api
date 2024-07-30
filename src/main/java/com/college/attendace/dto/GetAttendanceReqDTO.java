package com.college.attendace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor
public class GetAttendanceReqDTO {
    private Long subjectId;
    private LocalDate date;
}
