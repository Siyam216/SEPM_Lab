package com.example.student_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for returning student profile data to frontend
 * Excludes sensitive information like password
 * Currently unused - placeholder for future use
 * Could be used to return student data without exposing all entity fields
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileDTO {

    // Student's unique ID
    private Long id;

    // Student's full name
    private String name;

    // Student's email address
    private String email;

    // Student's roll number
    private String rollNumber;

    // Department name (not just ID)
    private String departmentName;

    // Current semester
    private Integer semester;

    // Phone number
    private String phone;

    // Account status
    private String status;

    // Note: Password is deliberately excluded for security
}
