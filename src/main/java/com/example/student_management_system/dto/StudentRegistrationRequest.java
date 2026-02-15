package com.example.student_management_system.dto;

import com.example.student_management_system.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for student registration requests
 * Receives student data from registration form
 * Creates new student with PENDING status (needs teacher approval)
 * Used in: AuthController.registerStudent() -> StudentService.registerStudent()
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegistrationRequest {

    // Student's full name (required)
    @NotBlank(message = "Name is required")
    private String name;

    // Student's email address - used for login (required, must be valid email)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    // Plain text password - will be BCrypt hashed before storing (min 6 characters)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    // Student's phone number (required)
    @NotBlank(message = "Phone is required")
    private String phone;

    // Unique student roll number (e.g., CS2024001)
    @NotBlank(message = "Roll number is required")
    private String rollNumber;

    // Student's residential address (optional)
    private String address;

    // Date of birth in string format (optional)
    private String dateOfBirth;

    // Gender: "MALE", "FEMALE", or "OTHER" (optional)
    private String gender;

    // Current semester number (optional)
    private Integer semester;

    // Parent/guardian's full name (optional)
    private String guardianName;

    // Parent/guardian's contact number (optional)
    private String guardianContact;

    // Department ID to assign student to (e.g., Computer Science department)
    private Long departmentId;
}
