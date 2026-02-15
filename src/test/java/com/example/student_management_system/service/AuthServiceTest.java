package com.example.student_management_system.service;

import com.example.student_management_system.config.JwtUtil;
import com.example.student_management_system.dto.*;
import com.example.student_management_system.entity.*;
import com.example.student_management_system.exception.DuplicateResourceException;
import com.example.student_management_system.exception.UnauthorizedException;
import com.example.student_management_system.repository.DepartmentRepository;
import com.example.student_management_system.repository.StudentRepository;
import com.example.student_management_system.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private Department department;
    private StudentRegistrationRequest studentRequest;
    private TeacherRegistrationRequest teacherRequest;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");
        department.setCode("CS");

        studentRequest = new StudentRegistrationRequest();
        studentRequest.setName("John Doe");
        studentRequest.setEmail("john@test.com");
        studentRequest.setPassword("password123");
        studentRequest.setRollNumber("CS001");
        studentRequest.setPhone("1234567890");
        studentRequest.setDepartmentId(1L);
        studentRequest.setSemester(2);

        teacherRequest = new TeacherRegistrationRequest();
        teacherRequest.setName("Dr. Smith");
        teacherRequest.setEmail("smith@test.com");
        teacherRequest.setPassword("password123");
        teacherRequest.setEmployeeId("EMP001");
        teacherRequest.setPhone("0987654321");
        teacherRequest.setDepartmentId(1L);
        teacherRequest.setSpecialization("AI");
    }

    @Test
    void testRegisterStudent_Success() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentRepository.existsByRollNumber(anyString())).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student s = invocation.getArgument(0);
            s.setId(1L);
            return s;
        });

        AuthResponse response = authService.registerStudent(studentRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("John Doe");
        assertThat(response.getEmail()).isEqualTo("john@test.com");
        assertThat(response.getStatus()).isEqualTo("PENDING");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testRegisterStudent_DuplicateEmail() {
        when(studentRepository.existsByEmail("john@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.registerStudent(studentRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email already registered");
    }

    @Test
    void testRegisterStudent_DuplicateRollNumber() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentRepository.existsByRollNumber("CS001")).thenReturn(true);

        assertThatThrownBy(() -> authService.registerStudent(studentRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Roll number already exists");
    }

    @Test
    void testRegisterTeacher_Success() {
        when(teacherRepository.existsByEmail(anyString())).thenReturn(false);
        when(teacherRepository.existsByEmployeeId(anyString())).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> {
            Teacher t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });

        AuthResponse response = authService.registerTeacher(teacherRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Dr. Smith");
        assertThat(response.getEmail()).isEqualTo("smith@test.com");
        assertThat(response.getStatus()).isEqualTo("ACTIVE");
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void testRegisterTeacher_DuplicateEmail() {
        when(teacherRepository.existsByEmail("smith@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.registerTeacher(teacherRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email already registered");
    }

    @Test
    void testRegisterTeacher_DuplicateEmployeeId() {
        when(teacherRepository.existsByEmail(anyString())).thenReturn(false);
        when(teacherRepository.existsByEmployeeId("EMP001")).thenReturn(true);

        assertThatThrownBy(() -> authService.registerTeacher(teacherRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Employee ID already exists");
    }
}
