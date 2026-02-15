package com.example.student_management_system.service;

import com.example.student_management_system.dto.TeacherRegistrationRequest;
import com.example.student_management_system.entity.Department;
import com.example.student_management_system.entity.Teacher;
import com.example.student_management_system.entity.User;
import com.example.student_management_system.exception.DuplicateResourceException;
import com.example.student_management_system.exception.ResourceNotFoundException;
import com.example.student_management_system.repository.DepartmentRepository;
import com.example.student_management_system.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;
    private Department department;
    private TeacherRegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");
        department.setCode("CS");

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Dr. Smith");
        teacher.setEmail("smith@test.com");
        teacher.setPassword("password123");
        teacher.setEmployeeId("EMP001");
        teacher.setPhone("1234567890");
        teacher.setRole(User.Role.TEACHER);
        teacher.setStatus(User.AccountStatus.ACTIVE);
        teacher.setDepartment(department);
        teacher.setSpecialization("AI");
        teacher.setQualification("PhD");
        teacher.setExperience(5);

        registrationRequest = new TeacherRegistrationRequest();
        registrationRequest.setName("Dr. Johnson");
        registrationRequest.setEmail("johnson@test.com");
        registrationRequest.setPassword("password123");
        registrationRequest.setEmployeeId("EMP002");
        registrationRequest.setPhone("0987654321");
        registrationRequest.setDepartmentId(1L);
        registrationRequest.setSpecialization("ML");
    }

    @Test
    void testGetAllTeachers() {
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher));

        List<Teacher> teachers = teacherService.getAllTeachers();

        assertThat(teachers).hasSize(1);
        assertThat(teachers.get(0).getName()).isEqualTo("Dr. Smith");
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void testGetTeacherById_Success() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Teacher found = teacherService.getTeacherById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Dr. Smith");
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTeacherById_NotFound() {
        when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teacherService.getTeacherById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Teacher not found");
    }

    @Test
    void testRegisterTeacher_Success() {
        when(teacherRepository.existsByEmail(anyString())).thenReturn(false);
        when(teacherRepository.existsByEmployeeId(anyString())).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        Teacher registered = teacherService.registerTeacher(registrationRequest);

        assertThat(registered).isNotNull();
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void testRegisterTeacher_DuplicateEmail() {
        when(teacherRepository.existsByEmail("johnson@test.com")).thenReturn(true);

        assertThatThrownBy(() -> teacherService.registerTeacher(registrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email already registered");
    }

    @Test
    void testRegisterTeacher_DuplicateEmployeeId() {
        when(teacherRepository.existsByEmail(anyString())).thenReturn(false);
        when(teacherRepository.existsByEmployeeId("EMP002")).thenReturn(true);

        assertThatThrownBy(() -> teacherService.registerTeacher(registrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Employee ID already exists");
    }

    @Test
    void testGetTeachersByDepartment() {
        when(teacherRepository.findByDepartmentId(1L)).thenReturn(Arrays.asList(teacher));

        List<Teacher> teachers = teacherService.getTeachersByDepartment(1L);

        assertThat(teachers).hasSize(1);
        verify(teacherRepository, times(1)).findByDepartmentId(1L);
    }

    @Test
    void testSearchTeachers() {
        when(teacherRepository.searchTeachers("Smith")).thenReturn(Arrays.asList(teacher));

        List<Teacher> results = teacherService.searchTeachers("Smith");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Dr. Smith");
    }

    @Test
    void testUpdateTeacher() {
        Teacher updatedDetails = new Teacher();
        updatedDetails.setName("Dr. Smith Updated");
        updatedDetails.setPhone("9999999999");

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        Teacher updated = teacherService.updateTeacher(1L, updatedDetails);

        assertThat(updated).isNotNull();
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void testDeleteTeacher() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        doNothing().when(teacherRepository).delete(teacher);

        teacherService.deleteTeacher(1L);

        verify(teacherRepository, times(1)).delete(teacher);
    }
}
