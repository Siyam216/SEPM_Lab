package com.example.student_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating student grades in an enrollment
 * Teacher assigns grade, marks, and status to student's course enrollment
 * All fields are optional - only update what's sent
 * Used in: EnrollmentController.updateGrade() -> EnrollmentService.updateGrade()
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeUpdateRequest {

    // Letter grade (e.g., "A", "B+", "C", "F")
    private String grade;

    // Numerical marks/score (e.g., 85.5, 92.0)
    private Double marks;

    // Enrollment status (e.g., "COMPLETED", "FAILED", "IN_PROGRESS")
    private String status;

    // Teacher's remarks or feedback for the student
    private String remarks;
}
