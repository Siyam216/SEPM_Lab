package com.example.student_management_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for teacher registration requests
 * Receives teacher data from registration form
 * Creates new teacher with ACTIVE status and returns JWT token immediately
 * Used in: AuthController.registerTeacher() -> TeacherService.registerTeacher()
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRegistrationRequest {

    // Teacher's full name (required)
    @NotBlank(message = "Name is required")
    private String name;

    // Teacher's email address - used for login (required, must be valid email)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    // Plain text password - will be BCrypt hashed before storing (min 6 characters)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    // Teacher's phone number (required)
    @NotBlank(message = "Phone is required")
    private String phone;

    // Unique employee ID (e.g., T001, T002)
    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    // Area of specialization (e.g., "Artificial Intelligence", "Database Systems")
    private String specialization;

    // Educational qualification (e.g., "Ph.D. in Computer Science")
    private String qualification;

    // Years of teaching/work experience
    private Integer experience;

    // Office room number (e.g., "CS-301")
    private String officeRoom;

    // Department ID to assign teacher to (e.g., Computer Science department)
    private Long departmentId;
}
