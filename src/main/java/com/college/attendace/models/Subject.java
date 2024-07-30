package com.college.attendace.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    @ManyToOne
    @JoinColumn(name="faculty_id", nullable=false)
    @JsonBackReference
    private Faculty faculty;

    private String code;
    private String comment;
    @ManyToMany
    @JsonManagedReference
    private List<Student> students;
}
