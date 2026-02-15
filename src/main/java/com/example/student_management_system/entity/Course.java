package com.example.student_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Course code is required")
    @Column(nullable = false, unique = true, length = 20)
    private String courseCode;

    @Column(length = 1000)
    private String description;

    private Integer credits;

    private Integer semester;

    @Enumerated(EnumType.STRING)
    private CourseType courseType;

    // Many Courses belong to One Department (Many-to-One)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    @JsonIgnoreProperties({"courses", "students", "teachers", "hibernateLazyInitializer", "handler"})
    private Department department;

    // Many Courses taught by One Teacher (Many-to-One)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    @JsonIgnoreProperties({"courses", "department", "password", "hibernateLazyInitializer", "handler"})
    private Teacher teacher;

    // Many-to-Many relationship with Student through Enrollment
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum CourseType {
        CORE, ELECTIVE, LAB, PROJECT
    }
}
