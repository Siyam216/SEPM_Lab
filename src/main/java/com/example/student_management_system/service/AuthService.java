package com.example.student_management_system.service;

import com.example.student_management_system.config.JwtUtil;
import com.example.student_management_system.dto.*;
import com.example.student_management_system.entity.*;
import com.example.student_management_system.exception.DuplicateResourceException;
import com.example.student_management_system.exception.ResourceNotFoundException;
import com.example.student_management_system.exception.UnauthorizedException;
import com.example.student_management_system.repository.DepartmentRepository;
import com.example.student_management_system.repository.StudentRepository;
import com.example.student_management_system.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse registerStudent(StudentRegistrationRequest request) {
        // Check for duplicate email and roll number
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }
        if (studentRepository.existsByRollNumber(request.getRollNumber())) {
            throw new DuplicateResourceException("Roll number already exists");
        }

        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setPhone(request.getPhone());
        student.setRollNumber(request.getRollNumber());
        student.setRole(User.Role.STUDENT);
        student.setStatus(User.AccountStatus.PENDING); // Pending approval
        student.setAddress(request.getAddress());
        student.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null) {
            student.setGender(Student.Gender.valueOf(request.getGender().toUpperCase()));
        }
        student.setSemester(request.getSemester());
        student.setGuardianName(request.getGuardianName());
        student.setGuardianContact(request.getGuardianContact());

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            student.setDepartment(department);
        }

        studentRepository.save(student);

        return AuthResponse.builder()
                .message("Registration successful. Waiting for teacher approval.")
                .id(student.getId())
                .email(student.getEmail())
                .name(student.getName())
                .role(student.getRole().name())
                .status(student.getStatus().name())
                .build();
    }

    @Transactional
    public AuthResponse registerTeacher(TeacherRegistrationRequest request) {
        // Check for duplicate email and employee ID
        if (teacherRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }
        if (teacherRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new DuplicateResourceException("Employee ID already exists");
        }

        Teacher teacher = new Teacher();
        teacher.setName(request.getName());
        teacher.setEmail(request.getEmail());
        teacher.setPassword(passwordEncoder.encode(request.getPassword()));
        teacher.setPhone(request.getPhone());
        teacher.setEmployeeId(request.getEmployeeId());
        teacher.setRole(User.Role.TEACHER);
        teacher.setStatus(User.AccountStatus.ACTIVE); // Teachers are auto-approved
        teacher.setSpecialization(request.getSpecialization());
        teacher.setQualification(request.getQualification());
        teacher.setExperience(request.getExperience());
        teacher.setOfficeRoom(request.getOfficeRoom());

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            teacher.setDepartment(department);
        }

        teacherRepository.save(teacher);

        String token = jwtUtil.generateToken(teacher.getEmail());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(teacher.getId())
                .email(teacher.getEmail())
                .name(teacher.getName())
                .role(teacher.getRole().name())
                .status(teacher.getStatus().name())
                .message("Teacher registration successful")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Find user (student or teacher)
        User user = studentRepository.findByEmail(request.getEmail())
                .map(student -> (User) student)
                .orElseGet(() -> teacherRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        // Check if account is active
        if (user.getStatus() == User.AccountStatus.PENDING) {
            throw new UnauthorizedException("Account is pending approval");
        }
        if (user.getStatus() == User.AccountStatus.SUSPENDED) {
            throw new UnauthorizedException("Account is suspended");
        }
        if (user.getStatus() == User.AccountStatus.REJECTED) {
            throw new UnauthorizedException("Account registration was rejected");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .message("Login successful")
                .build();
    }
}
