package com.college.attendace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class MakeAttendanceDTO {
    private String qrCode;
    private Long userId;
}
