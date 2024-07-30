package com.college.attendace.dao;

import com.college.attendace.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceDAO extends JpaRepository<Attendance, Long> {
    List<Attendance> findBySubject_IdAndStudent_User_Email(Long id, String email);
    List<Attendance> findBySubject_Id(Long id);
    List<Attendance> findBySubject_IdAndStudent_Id(Long id, Long id1);
    List<Attendance> findBySubject_IdAndDate(Long id, LocalDate date);
    List<Attendance> findByDate(LocalDate date);
}
