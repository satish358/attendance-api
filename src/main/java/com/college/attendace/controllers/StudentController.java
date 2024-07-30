package com.college.attendace.controllers;

import com.college.attendace.dao.AttendanceDAO;
import com.college.attendace.dao.AttendanceQRDetailsDAO;
import com.college.attendace.dao.StudentDAO;
import com.college.attendace.dao.SubjectDAO;
import com.college.attendace.dto.BasicResponseDTO;
import com.college.attendace.dto.MakeAttendanceDTO;
import com.college.attendace.exceptions.AttendanceTimeEndedException;
import com.college.attendace.exceptions.StudentNotFoundException;
import com.college.attendace.exceptions.UserNotEnrollToSubjectException;
import com.college.attendace.models.AttendaceQRDetails;
import com.college.attendace.models.Attendance;
import com.college.attendace.models.Student;
import com.college.attendace.models.Subject;
import com.college.attendace.utils.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/student")
public class StudentController {
    @Autowired
    SubjectDAO subjectRep;
    @Autowired
    StudentDAO studentRep;
    @Autowired
    AttendanceQRDetailsDAO aqr;
    @Autowired
    AttendanceDAO attendanceDAO;

    @Autowired
    private JWTUtil jwtUtil;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/makeAttendance/{qrCode}")
    public ResponseEntity<BasicResponseDTO<Attendance>> makeAttendance(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@PathVariable("qrCode") String qrCode){
        Optional<AttendaceQRDetails> atDetails = aqr.findByQrUID(qrCode);
        String userEmail = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        if(atDetails.isEmpty())
            throw new AttendanceTimeEndedException();
        Optional<Student> student_ = studentRep.findByUser_Email(userEmail);
        if(student_.isEmpty())
            throw new StudentNotFoundException();
       if(!subjectRep.existsByStudents_User_Email(userEmail))
           throw new UserNotEnrollToSubjectException();

        Attendance at = new Attendance();
        at.setDate(LocalDate.now());
        at.setSubject(atDetails.get().getSubject());
        at.setTime(LocalTime.now());
        at.setStudent(student_.get());
        attendanceDAO.save(at);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Attendance Added", at), HttpStatus.CREATED);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/attendance/getAllBySubject/{subjectId}")
    public ResponseEntity<BasicResponseDTO<List<Attendance>>> getAllAttendanceBySubjectId(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable("subjectId") Long subjectId){
        String userEmail = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        List<Attendance> s = attendanceDAO.findBySubject_IdAndStudent_User_Email(subjectId, userEmail);
        return ResponseEntity.ok(new BasicResponseDTO<>(false, "All data", s));
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/subjects")
    public ResponseEntity<BasicResponseDTO<List<Subject>>> getAllStudentByStudentId( @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        String userEmail = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        List<Subject> s = subjectRep.findByStudents_User_Email(userEmail);
        return ResponseEntity.ok(new BasicResponseDTO<>(false, "All data", s));
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/profile")
    public ResponseEntity<BasicResponseDTO<Student>> getStudentById( @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        String userEmail = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        Optional<Student> s = studentRep.findByUser_Email(userEmail);
        if(s.isEmpty()) throw new StudentNotFoundException();
        return ResponseEntity.ok(new BasicResponseDTO<>(false, "All data", s.get()));
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/enroll/{subjectId}")
    public ResponseEntity<BasicResponseDTO<Subject>> getStudentById( @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable("subjectId") Long subjectId){
        String userEmail = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        Optional<Student> s = studentRep.findByUser_Email(userEmail);
        if(s.isEmpty()) throw new StudentNotFoundException();
        Optional<Subject> sub = subjectRep.findById(subjectId);
        if(sub.isEmpty()) throw new StudentNotFoundException();
        Subject sb = sub.get();
        sb.getStudents().add(s.get());
        subjectRep.save(sb);
        return ResponseEntity.ok(new BasicResponseDTO<>(false, "All data", sb));
    }
}
