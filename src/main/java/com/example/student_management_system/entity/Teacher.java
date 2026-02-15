package com.example.student_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Teacher extends User {

    @Column(unique = true, nullable = false)
    private String employeeId;

    private String specialization;

    private String qualification;

    private Integer experience; // in years

    private String officeRoom;

    // Many Teachers belong to One Department (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department;

    // One Teacher teaches many Courses (One-to-Many)
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @JsonIgnore
    @Builder.Default
    private List<Course> courses = new ArrayList<>();
}
