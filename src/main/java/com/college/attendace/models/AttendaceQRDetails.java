package com.college.attendace.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class AttendaceQRDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private String qrUID;
    @OneToOne
    private Subject subject;
    @Column(nullable = false)
    private LocalDateTime createdOn;
    @PrePersist
    public void setDefaultCreatedDate() {
        createdOn = LocalDateTime.now();
        qrUID =  "SU" + subject.getId() + "RI" + ( ((Double)(Math.random() * 100000 )).intValue() );
    }
}
