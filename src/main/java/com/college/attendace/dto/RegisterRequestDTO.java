package com.college.attendace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import com.college.attendace.enums.UserRoleEnum;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;
    @NotBlank
    private UserRoleEnum role;

    //STUDENT ONLY
    private String college;
    private String prn;
    private Long age;
    private String course;

    // FACULTY ONLY
    //college
    private LocalDate joiningDate;
}
