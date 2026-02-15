package com.example.student_management_system.service;

import com.example.student_management_system.entity.Department;
import com.example.student_management_system.exception.DuplicateResourceException;
import com.example.student_management_system.exception.ResourceNotFoundException;
import com.example.student_management_system.repository.DepartmentRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");
        department.setCode("CS");
        department.setDescription("Department of Computer Science");
    }

    @Test
    void testGetAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department));

        List<Department> departments = departmentService.getAllDepartments();

        assertThat(departments).hasSize(1);
        assertThat(departments.get(0).getName()).isEqualTo("Computer Science");
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testGetDepartmentById_Success() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Department found = departmentService.getDepartmentById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Computer Science");
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDepartmentById_NotFound() {
        when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.getDepartmentById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department not found");
    }

    @Test
    void testGetDepartmentByCode_Success() {
        when(departmentRepository.findByCode("CS")).thenReturn(Optional.of(department));

        Department found = departmentService.getDepartmentByCode("CS");

        assertThat(found).isNotNull();
        assertThat(found.getCode()).isEqualTo("CS");
        verify(departmentRepository, times(1)).findByCode("CS");
    }

    @Test
    void testCreateDepartment_Success() {
        when(departmentRepository.existsByName(anyString())).thenReturn(false);
        when(departmentRepository.existsByCode(anyString())).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Department created = departmentService.createDepartment(department);

        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("Computer Science");
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void testCreateDepartment_DuplicateName() {
        when(departmentRepository.existsByName("Computer Science")).thenReturn(true);

        assertThatThrownBy(() -> departmentService.createDepartment(department))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void testCreateDepartment_DuplicateCode() {
        when(departmentRepository.existsByName(anyString())).thenReturn(false);
        when(departmentRepository.existsByCode("CS")).thenReturn(true);

        assertThatThrownBy(() -> departmentService.createDepartment(department))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void testUpdateDepartment_Success() {
        Department updated = new Department();
        updated.setDescription("Updated description");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Department result = departmentService.updateDepartment(1L, updated);

        assertThat(result).isNotNull();
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void testDeleteDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        doNothing().when(departmentRepository).delete(department);

        departmentService.deleteDepartment(1L);

        verify(departmentRepository, times(1)).delete(department);
    }

    @Test
    void testSearchDepartments() {
        when(departmentRepository.searchDepartments("Computer")).thenReturn(Arrays.asList(department));

        List<Department> results = departmentService.searchDepartments("Computer");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).contains("Computer");
    }
}
