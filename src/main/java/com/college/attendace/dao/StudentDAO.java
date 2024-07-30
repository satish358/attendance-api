package com.college.attendace.dao;

import com.college.attendace.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentDAO extends JpaRepository<Student, Long> {
    Optional<Student> findByUser_Email(String email);
    List<Student> findByCourse(String course);
    Optional<Student> findByUser_Id(Long id);
}
