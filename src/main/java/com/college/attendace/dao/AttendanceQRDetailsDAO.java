package com.college.attendace.dao;

import com.college.attendace.models.AttendaceQRDetails;
import com.college.attendace.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceQRDetailsDAO extends JpaRepository<AttendaceQRDetails, Long> {
    Optional<AttendaceQRDetails> findByQrUID(String qrUID);
    boolean existsByQrUID(String qrUID);
    long deleteBySubject(Subject subject);
    Optional<AttendaceQRDetails> findBySubject_Id(Long id);
}
