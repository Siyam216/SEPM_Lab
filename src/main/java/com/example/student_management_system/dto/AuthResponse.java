package com.example.student_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication responses
 * Sent to frontend after successful login or registration
 * Contains JWT token and user information
 * Used in: AuthService.login(), registerStudent(), registerTeacher()
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    // JWT token for authenticated requests (24-hour expiration)
    private String token;

    // Token type - always "Bearer" for JWT authentication
    private String type = "Bearer";

    // User's unique database ID
    private Long id;

    // User's email address
    private String email;

    // User's full name
    private String name;

    // User's role: "STUDENT" or "TEACHER"
    private String role;

    // Account status: "ACTIVE", "PENDING", "SUSPENDED", or "REJECTED"
    private String status;

    // Success or error message for the user
    private String message;
}
