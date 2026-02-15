package com.example.student_management_system.service;

import com.example.student_management_system.dto.StudentRegistrationRequest;
import com.example.student_management_system.entity.Department;
import com.example.student_management_system.entity.Student;
import com.example.student_management_system.entity.User;
import com.example.student_management_system.exception.DuplicateResourceException;
import com.example.student_management_system.exception.ResourceNotFoundException;
import com.example.student_management_system.repository.DepartmentRepository;
import com.example.student_management_system.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private Department department;
    private StudentRegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");
        department.setCode("CS");

        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.setEmail("john@test.com");
        student.setPassword("password123");
        student.setRollNumber("CS001");
        student.setPhone("1234567890");
        student.setRole(User.Role.STUDENT);
        student.setStatus(User.AccountStatus.ACTIVE);
        student.setDepartment(department);
        student.setSemester(3);
        student.setDateOfBirth(String.valueOf(LocalDate.of(2000, 1, 1)));

        registrationRequest = new StudentRegistrationRequest();
        registrationRequest.setName("Jane Doe");
        registrationRequest.setEmail("jane@test.com");
        registrationRequest.setPassword("password123");
        registrationRequest.setRollNumber("CS002");
        registrationRequest.setPhone("0987654321");
        registrationRequest.setDepartmentId(1L);
        registrationRequest.setSemester(2);
        registrationRequest.setDateOfBirth(String.valueOf(LocalDate.of(2001, 5, 15)));
    }

    @Test
    void testGetAllStudents() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));

        List<Student> students = studentService.getAllStudents();

        assertThat(students).hasSize(1);
        assertThat(students.get(0).getName()).isEqualTo("John Doe");
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student found = studentService.getStudentById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("John Doe");
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStudentById_NotFound() {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student not found");
    }

    @Test
    void testRegisterStudent_Success() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentRepository.existsByRollNumber(anyString())).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student registered = studentService.registerStudent(registrationRequest);

        assertThat(registered).isNotNull();
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testRegisterStudent_DuplicateEmail() {
        when(studentRepository.existsByEmail("jane@test.com")).thenReturn(true);

        assertThatThrownBy(() -> studentService.registerStudent(registrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email already registered");
    }

    @Test
    void testRegisterStudent_DuplicateRollNumber() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentRepository.existsByRollNumber("CS002")).thenReturn(true);

        assertThatThrownBy(() -> studentService.registerStudent(registrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Roll number already exists");
    }

    @Test
    void testGetPendingStudents() {
        Student pendingStudent = new Student();
        pendingStudent.setStatus(User.AccountStatus.PENDING);
        when(studentRepository.findByStatus(User.AccountStatus.PENDING))
                .thenReturn(Arrays.asList(pendingStudent));

        List<Student> pending = studentService.getPendingStudents();

        assertThat(pending).hasSize(1);
        assertThat(pending.get(0).getStatus()).isEqualTo(User.AccountStatus.PENDING);
    }

    @Test
    void testApproveStudent() {
        student.setStatus(User.AccountStatus.PENDING);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student approved = studentService.approveStudent(1L);

        assertThat(approved.getStatus()).isEqualTo(User.AccountStatus.ACTIVE);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testRejectStudent() {
        student.setStatus(User.AccountStatus.PENDING);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student rejected = studentService.rejectStudent(1L);

        assertThat(rejected.getStatus()).isEqualTo(User.AccountStatus.REJECTED);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testDeleteStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).delete(student);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).delete(student);
    }

    @Test
    void testSearchStudents() {
        when(studentRepository.searchStudents("John")).thenReturn(Arrays.asList(student));

        List<Student> results = studentService.searchStudents("John");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("John Doe");
    }

    @Test
    void testGetStudentsByDepartment() {
        when(studentRepository.findByDepartmentId(1L)).thenReturn(Arrays.asList(student));

        List<Student> students = studentService.getStudentsByDepartment(1L);

        assertThat(students).hasSize(1);
        verify(studentRepository, times(1)).findByDepartmentId(1L);
    }
}
