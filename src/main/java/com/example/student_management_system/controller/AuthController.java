package com.example.student_management_system.controller;

import com.example.student_management_system.dto.AuthResponse;
import com.example.student_management_system.dto.LoginRequest;
import com.example.student_management_system.dto.StudentRegistrationRequest;
import com.example.student_management_system.dto.TeacherRegistrationRequest;
import com.example.student_management_system.entity.Student;
import com.example.student_management_system.entity.Teacher;
import com.example.student_management_system.service.AuthService;
import com.example.student_management_system.service.StudentService;
import com.example.student_management_system.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and registration endpoints")
public class AuthController {

    private final AuthService authService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    @PostMapping("/login")
    @Operation(summary = "Login user (student or teacher)")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/register/student")
    @Operation(summary = "Register a new student")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody StudentRegistrationRequest request) {
        try {
            Student student = studentService.registerStudent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(student);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/register/teacher")
    @Operation(summary = "Register a new teacher")
    public ResponseEntity<?> registerTeacher(@Valid @RequestBody TeacherRegistrationRequest request) {
        try {
            Teacher teacher = teacherService.registerTeacher(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(teacher);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/pending-students")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Get all pending student registrations")
    public ResponseEntity<List<Student>> getPendingStudents() {
        List<Student> students = studentService.getPendingStudents();
        return ResponseEntity.ok(students);
    }

    @PostMapping("/approve-student/{studentId}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Approve a student registration")
    public ResponseEntity<Student> approveStudent(@PathVariable Long studentId) {
        Student student = studentService.approveStudent(studentId);
        return ResponseEntity.ok(student);
    }

    // Changes student status to REJECTED, preventing them from logging in
    @PostMapping("/reject-student/{studentId}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Reject a student registration")
    public ResponseEntity<Student> rejectStudent(@PathVariable Long studentId) {
        Student student = studentService.rejectStudent(studentId);
        return ResponseEntity.ok(student);
    }

    // Simple error response class
    static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
