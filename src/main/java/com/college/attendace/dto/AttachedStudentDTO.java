package com.college.attendace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachedStudentDTO {
    private List<Long> studentIds;
    private Long subjectId;
}
