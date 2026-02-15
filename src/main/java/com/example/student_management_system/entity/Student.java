package com.example.student_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Student extends User {

    @Column(unique = true, nullable = false)
    private String rollNumber;

    private String address;

    private String dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer semester;

    private String guardianName;

    private String guardianContact;

    // Many Students belong to One Department (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department;

    // Many-to-Many relationship with Course through Enrollment
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();

    // One-to-One relationship with StudentProfile
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private StudentProfile profile;

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
