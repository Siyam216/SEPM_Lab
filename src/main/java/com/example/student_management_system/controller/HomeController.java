package com.example.student_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Serves the login page as the home page
    @GetMapping("/")
    public String home() {
        return "login";
    }

    // Serves the registration page
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // Serves the student dashboard page
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    // Serves the user profile page
    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    // Serves the browse courses page
    @GetMapping("/courses")
    public String courses() {
        return "courses";
    }

    // Serves the my enrolled courses page
    @GetMapping("/my-courses")
    public String myCourses() {
        return "my-courses";
    }

    // Serves the debug students page (for development/testing)
    @GetMapping("/debug/students")
    public String debugStudents() {
        return "debug-students";
    }

    // Serves the teacher dashboard page
    @GetMapping("/teacher-dashboard")
    public String teacherDashboard() {
        return "teacher-dashboard";
    }

    // Serves the grade management page (for teachers)
    @GetMapping("/grade-management")
    public String gradeManagement() {
        return "grade-management";
    }

    // Serves the student approvals page (for teachers)
    @GetMapping("/student-approvals")
    public String studentApprovals() {
        return "student-approvals";
    }

    // Serves the database schema visualization page
    @GetMapping("/database-schema")
    public String databaseSchema() {
        return "database-schema";
    }
}
