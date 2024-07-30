package com.college.attendace.exceptions;

import com.college.attendace.dto.BasicResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class CentralizedException {

    @ExceptionHandler(UserAlreadyPresentException.class)
    public ResponseEntity<BasicResponseDTO<String>> handleUserAlreadyPresentException(UserAlreadyPresentException e){
        return new ResponseEntity<>(new BasicResponseDTO<>(false, e.getMessage(), null), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ConfirmPasswordNotMatchedException.class)
    public ResponseEntity<BasicResponseDTO<String>> handleConfirmPasswordNotMatchedException(ConfirmPasswordNotMatchedException e){
        return new ResponseEntity<>(new BasicResponseDTO<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(SubjectNotFoundException.class)
    public ResponseEntity<BasicResponseDTO<String>> handleSubjectNotFoundException(SubjectNotFoundException e){
        return new ResponseEntity<>(new BasicResponseDTO<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AttendaceDetailsNotFoundException.class)
    public ResponseEntity<BasicResponseDTO<String>> handleAttendaceDetailsNotFoundException(AttendaceDetailsNotFoundException e){
        return new ResponseEntity<>(new BasicResponseDTO<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<BasicResponseDTO<String>> handleStudentNotFoundException(StudentNotFoundException e){
        return new ResponseEntity<>(new BasicResponseDTO<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AttendanceTimeEndedException.class)
    public ResponseEntity<BasicResponseDTO<String>> handleAttendanceTimeEndedException(AttendanceTimeEndedException e){
        return new ResponseEntity<>(new BasicResponseDTO<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotEnrollToSubjectException.class)
    public ResponseEntity<BasicResponseDTO<String>> handleUserNotEnrollToSubjectException(UserNotEnrollToSubjectException e){
        return new ResponseEntity<>(new BasicResponseDTO<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
    }


}
