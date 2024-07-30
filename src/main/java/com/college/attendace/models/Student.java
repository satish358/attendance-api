package com.college.attendace.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String college;
    private String prn;
    private Long age;
    private String course;
    @OneToOne
    private User user;
    @ManyToMany(mappedBy = "students")
    @JsonBackReference
    private List<Subject> subjects;

}
