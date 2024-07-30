package com.college.attendace.dao;

import com.college.attendace.models.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyDAO extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByUser_Email(String email);
}
