package com.example.student_management_system.controller;

import com.example.student_management_system.entity.Teacher;
import com.example.student_management_system.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@Tag(name = "Teachers", description = "Teacher management endpoints")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Get all teachers")
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    // Retrieves specific teacher details by their unique ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Get teacher by ID")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        Teacher teacher = teacherService.getTeacherById(id);
        return ResponseEntity.ok(teacher);
    }

    // Filters teachers by their department (e.g., Computer Science)
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Get teachers by department")
    public ResponseEntity<List<Teacher>> getTeachersByDepartment(@PathVariable Long departmentId) {
        List<Teacher> teachers = teacherService.getTeachersByDepartment(departmentId);
        return ResponseEntity.ok(teachers);
    }

    // Searches teachers by name or employee ID using keyword
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Search teachers by name or employee ID")
    public ResponseEntity<List<Teacher>> searchTeachers(@RequestParam String keyword) {
        List<Teacher> teachers = teacherService.searchTeachers(keyword);
        return ResponseEntity.ok(teachers);
    }

    // Updates teacher profile information (only teachers can update)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Update teacher profile (Teacher only)")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        Teacher updatedTeacher = teacherService.updateTeacher(id, teacher);
        return ResponseEntity.ok(updatedTeacher);
    }

    // Removes teacher from the system (only teachers can perform this action)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Delete teacher (Teacher only)")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
