package com.example.student_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for enrolling a student in a course
 * Creates a new enrollment record linking student to course
 * Used in: EnrollmentController.enrollStudent() -> EnrollmentService.enrollStudent()
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequest {

    // ID of the student to enroll
    private Long studentId;

    // ID of the course to enroll in
    private Long courseId;

    // Academic year for this enrollment (e.g., "2024-2025")
    @NotBlank(message = "Academic year is required")
    private String academicYear;

    // Semester number when taking this course
    private Integer semester;
}
