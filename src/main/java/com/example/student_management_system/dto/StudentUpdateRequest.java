package com.example.student_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating student profile
 * All fields are optional - only sent fields will be updated
 * Cannot update: email, password, rollNumber, role, status (security)
 * Used in: StudentController.updateStudent() -> StudentService.updateStudent()
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateRequest {

    // Updated student name (optional)
    private String name;

    // Updated phone number (optional)
    private String phone;

    // Updated residential address (optional)
    private String address;

    // Updated date of birth (optional)
    private String dateOfBirth;

    // Updated gender (optional)
    private String gender;

    // Updated current semester (optional)
    private Integer semester;

    // Updated guardian name (optional)
    private String guardianName;

    // Updated guardian contact (optional)
    private String guardianContact;

    // Change department assignment (optional)
    private Long departmentId;
}
