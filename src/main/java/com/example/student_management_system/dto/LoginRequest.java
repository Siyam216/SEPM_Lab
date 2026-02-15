package com.example.student_management_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user login requests
 * Receives email and password from frontend, validates format
 * Used in: AuthController.login() -> AuthService.login()
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    // User's email address - must be valid email format
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    // User's plain text password - will be verified against BCrypt hash in database
    @NotBlank(message = "Password is required")
    private String password;
}
