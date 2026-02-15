package com.example.student_management_system.controller;

import com.example.student_management_system.dto.EnrollmentRequest;
import com.example.student_management_system.dto.GradeUpdateRequest;
import com.example.student_management_system.entity.Enrollment;
import com.example.student_management_system.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollments", description = "Student course enrollment and grade management")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // Returns all student-course enrollments in the system
    @GetMapping
    @Operation(summary = "Get all enrollments")
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    // Retrieves specific enrollment details by unique ID
    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        Enrollment enrollment = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(enrollment);
    }

    // Gets all courses a specific student is enrolled in
    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get enrollments by student")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        return ResponseEntity.ok(enrollments);
    }

    // Gets all students enrolled in a specific course
    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get enrollments by course")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
        return ResponseEntity.ok(enrollments);
    }

    // Filters enrollments by course and academic year
    @GetMapping("/course/{courseId}/year/{academicYear}")
    @Operation(summary = "Get enrollments by course and academic year")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourseAndYear(
            @PathVariable Long courseId,
            @PathVariable String academicYear) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseAndYear(courseId, academicYear);
        return ResponseEntity.ok(enrollments);
    }

    // Enrolls a student in a course
    @PostMapping
    @Operation(summary = "Enroll a student in a course")
    public ResponseEntity<Enrollment> enrollStudent(@Valid @RequestBody EnrollmentRequest request) {
        Enrollment enrollment = enrollmentService.enrollStudent(request);
        return new ResponseEntity<>(enrollment, HttpStatus.CREATED);
    }

    // Updates the grade for a student's enrollment (teacher assigns grade)
    @PutMapping("/{id}/grade")
    @Operation(summary = "Update grade for an enrollment")
    public ResponseEntity<Enrollment> updateGrade(@PathVariable Long id, @Valid @RequestBody GradeUpdateRequest request) {
        Enrollment enrollment = enrollmentService.updateGrade(id, request);
        return ResponseEntity.ok(enrollment);
    }

    // Removes enrollment (student drops a course)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete/Drop enrollment")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
