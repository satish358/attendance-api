package com.college.attendace.dao;

import com.college.attendace.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectDAO extends JpaRepository<Subject, Long> {
    boolean existsByStudents_User_Email(String email);
    List<Subject> findByStudents_User_Email(String email);
    List<Subject> findByStudents_Id(Long id);
}
