package com.college.attendace.controllers;

import com.college.attendace.dao.*;
import com.college.attendace.dto.*;
import com.college.attendace.exceptions.FacultyNotFoundException;
import com.college.attendace.exceptions.StudentNotFoundException;
import com.college.attendace.exceptions.SubjectNotFoundException;
import com.college.attendace.models.*;
import com.college.attendace.utils.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/faculty")
public class FacultyController {
    @Autowired
    FacultyDAO facultyDAO;
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


    @PostMapping("/subject/create")
    public ResponseEntity<BasicResponseDTO<Subject>> createSubject(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody RegisterSubjectDTO r){
        String userEmail = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        Optional<Faculty> faculty_ = facultyDAO.findByUser_Email(userEmail);
        if(faculty_.isEmpty()){
            throw new FacultyNotFoundException();
        }
        String code = "SUB"+(((Double)Math.random() ).intValue() * 10000);
        Subject s = new Subject(null, r.getName(), faculty_.get(), code, r.getComment(), new ArrayList<>());
        subjectRep.save(s);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "", s), HttpStatus.CREATED);
    }

    @PostMapping("/subject/attache-student")
    public ResponseEntity<BasicResponseDTO<String>> attachedStudentToSubject(@RequestBody AttachedStudentDTO r){
        List<Student> students = studentRep.findAllById(r.getStudentIds());
        Optional<Subject> subject_ = subjectRep.findById(r.getSubjectId());
        if(subject_.isEmpty()){
            throw new SubjectNotFoundException();
        }
        Subject s = subject_.get();
        s.setStudents(students);
        subjectRep.save(s);
        return ResponseEntity.ok(new BasicResponseDTO<>(false, "Students added", null));
    }
    @GetMapping("/subject/getAll")
    public ResponseEntity<BasicResponseDTO<List<Subject>>> getAllSubjects(){
        List<Subject> s = subjectRep.findAll();
        return ResponseEntity.ok(new BasicResponseDTO<>(false, "All data", s));
    }
    @GetMapping("/attendance/start/{subjectId}")
    public ResponseEntity<BasicResponseDTO<AttendaceQRDetails>> startAttendance(@PathVariable("subjectId") Long subjectId){
        Optional<Subject> subject_ = subjectRep.findById(subjectId);
        if(subject_.isEmpty()){
            throw new SubjectNotFoundException();
        }
        AttendaceQRDetails ad = new AttendaceQRDetails();
        ad.setSubject(subject_.get());
        aqr.save(ad);
        return ResponseEntity.ok(new BasicResponseDTO<>(false, "Attendance Started", ad));
    }
    @Transactional
    @GetMapping("/attendance/stop/{subjectId}")
    public ResponseEntity<BasicResponseDTO<Long>> stopAttendance(@PathVariable("subjectId") Long subjectId){
        Optional<Subject> subject_ = subjectRep.findById(subjectId);
        if(subject_.isEmpty()){
            throw new SubjectNotFoundException();
        }
        Long  ad = aqr.deleteBySubject(subject_.get());
        return ResponseEntity.ok(new BasicResponseDTO<>(false, "Attendance stopped", ad));
    }
    @GetMapping("/attendance/getAll")
    public ResponseEntity<BasicResponseDTO<List<Attendance>>> getAllAttendance(){
        List<Attendance> s = attendanceDAO.findAll();
        return ResponseEntity.ok(new BasicResponseDTO<>(false, "All data", s));
    }
    @PostMapping("/attendance/getAllBySubject")
    public ResponseEntity<BasicResponseDTO<List<ViewAttendanceDTO>>> getAllAttendanceBySubjectId(@RequestBody GetAttendanceReqDTO r){
        Optional<Subject> sub_ = subjectRep.findById(r.getSubjectId());
        if(sub_.isEmpty()) throw new SubjectNotFoundException();
        var sub = sub_.get();
        List<Attendance> s = attendanceDAO.findBySubject_IdAndDate(r.getSubjectId(), r.getDate());
        List<ViewAttendanceDTO> result =  sub.getStudents().stream().map(student -> {
            boolean es = s.stream().filter(ex -> ex.getStudent().equals(student)).collect(Collectors.toList()).isEmpty();
            return new ViewAttendanceDTO(
                    es ? "ABSENT" : "PRESENT",
                    r.getDate(),
                    student.getPrn(),
                    student.getUser().getFirstName() + " " + student.getUser().getLastName()
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(new BasicResponseDTO<>(false, "All data", result));
    }
    @GetMapping("/attendance/getDatesBySubject/{subjectId}")
    public ResponseEntity<BasicResponseDTO<List<Attendance>>> getDatesAttendanceBySubjectId(@PathVariable("subjectId") Long subjectId){
        List<Attendance> s = attendanceDAO.findBySubject_Id(subjectId);
        return ResponseEntity.ok(new BasicResponseDTO<>(false, "All data", s));
    }

    @GetMapping("/students/getAll")
    public ResponseEntity<BasicResponseDTO<List<Student>>> getAllStudents(){
        List<Student> s = studentRep.findAll();
        return ResponseEntity.ok(new BasicResponseDTO<>(false, "All data", s));
    }
    @GetMapping("/students/getAll/byCourse/{course}")
    public ResponseEntity<BasicResponseDTO<List<Student>>> getAllStudentsByCourse(@PathVariable("course") String course){
        List<Student> s = studentRep.findByCourse(course);
        return ResponseEntity.ok(new BasicResponseDTO<>(false, "All data", s));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/profile")
    public ResponseEntity<BasicResponseDTO<Faculty>> getFacultyProfile( @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        String userEmail = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        Optional<Faculty> s = facultyDAO.findByUser_Email(userEmail);
        if(s.isEmpty()) throw new FacultyNotFoundException();
        return ResponseEntity.ok(new BasicResponseDTO<>(false, "All data", s.get()));
    }

}
