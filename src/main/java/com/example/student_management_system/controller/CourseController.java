package com.example.student_management_system.controller;

import com.example.student_management_system.entity.Course;
import com.example.student_management_system.service.CourseService;
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
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Course management endpoints")
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/all")
    @Operation(summary = "Get all courses (Public)")
    public ResponseEntity<List<Course>> getAllCoursesPublic() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Get all courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/code/{courseCode}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Get course by course code")
    public ResponseEntity<Course> getCourseByCourseCode(@PathVariable String courseCode) {
        Course course = courseService.getCourseByCourseCode(courseCode);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Get courses by department")
    public ResponseEntity<List<Course>> getCoursesByDepartment(@PathVariable Long departmentId) {
        List<Course> courses = courseService.getCoursesByDepartment(departmentId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Get courses by teacher")
    public ResponseEntity<List<Course>> getCoursesByTeacher(@PathVariable Long teacherId) {
        List<Course> courses = courseService.getCoursesByTeacher(teacherId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/semester/{semester}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Get courses by semester")
    public ResponseEntity<List<Course>> getCoursesBySemester(@PathVariable Integer semester) {
        List<Course> courses = courseService.getCoursesBySemester(semester);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @Operation(summary = "Search courses by name or code")
    public ResponseEntity<List<Course>> searchCourses(@RequestParam String keyword) {
        List<Course> courses = courseService.searchCourses(keyword);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/unassigned")
    @Operation(summary = "Get courses without assigned teacher")
    public ResponseEntity<List<Course>> getUnassignedCourses() {
        List<Course> courses = courseService.getUnassignedCourses();
        return ResponseEntity.ok(courses);
    }

    // Creates a new course in the system (only teachers allowed)
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Create a new course (Teacher only)")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    // Updates existing course details (only teachers allowed)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Update course (Teacher only)")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @Valid @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourse(id, course);
        return ResponseEntity.ok(updatedCourse);
    }

    // Assigns a teacher to an unassigned course
    @PutMapping("/{courseId}/assign-teacher/{teacherId}")
    @Operation(summary = "Assign teacher to a course")
    public ResponseEntity<Course> assignTeacherToCourse(
            @PathVariable Long courseId,
            @PathVariable Long teacherId) {
        Course updatedCourse = courseService.assignTeacherToCourse(courseId, teacherId);
        return ResponseEntity.ok(updatedCourse);
    }

    // Removes course from the system (only teachers allowed)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course (Teacher only)")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
