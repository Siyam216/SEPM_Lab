package com.example.student_management_system.controller;

import com.example.student_management_system.entity.Department;
import com.example.student_management_system.service.DepartmentService;
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
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Departments", description = "Department management endpoints")
public class DepartmentController {

    private final DepartmentService departmentService;

    // Public endpoint to get all departments without authentication (for registration page)
    @GetMapping("/all")
    @Operation(summary = "Get all departments (Public)")
    public ResponseEntity<List<Department>> getAllDepartmentsPublic() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    // Returns list of all departments (requires authentication)
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Get all departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    // Retrieves specific department details by unique ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Get department by ID")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Department department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }

    // Finds department by its code (e.g., CS, EE, ME)
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Get department by code")
    public ResponseEntity<Department> getDepartmentByCode(@PathVariable String code) {
        Department department = departmentService.getDepartmentByCode(code);
        return ResponseEntity.ok(department);
    }

    // Searches departments by name or code using keyword
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Search departments by name or code")
    public ResponseEntity<List<Department>> searchDepartments(@RequestParam String keyword) {
        List<Department> departments = departmentService.searchDepartments(keyword);
        return ResponseEntity.ok(departments);
    }

    // Creates a new department (only teachers allowed)
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Create a new department (Teacher only)")
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department department) {
        Department createdDepartment = departmentService.createDepartment(department);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }

    // Updates existing department details (only teachers allowed)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Update department (Teacher only)")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @Valid @RequestBody Department department) {
        Department updatedDepartment = departmentService.updateDepartment(id, department);
        return ResponseEntity.ok(updatedDepartment);
    }

    // Removes department from the system (only teachers allowed)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Delete department (Teacher only)")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
