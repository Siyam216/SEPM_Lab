package com.example.student_management_system.service;

import com.example.student_management_system.entity.Department;
import com.example.student_management_system.exception.DuplicateResourceException;
import com.example.student_management_system.exception.ResourceNotFoundException;
import com.example.student_management_system.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    public Department getDepartmentByCode(String code) {
        return departmentRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with code: " + code));
    }

    public List<Department> searchDepartments(String keyword) {
        return departmentRepository.searchDepartments(keyword);
    }

    @Transactional
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new DuplicateResourceException("Department with name '" + department.getName() + "' already exists");
        }
        if (departmentRepository.existsByCode(department.getCode())) {
            throw new DuplicateResourceException("Department with code '" + department.getCode() + "' already exists");
        }

        return departmentRepository.save(department);
    }

    @Transactional
    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = getDepartmentById(id);

        // Check for duplicate name if changed
        if (departmentDetails.getName() != null && !departmentDetails.getName().equals(department.getName())) {
            if (departmentRepository.existsByName(departmentDetails.getName())) {
                throw new DuplicateResourceException("Department with name '" + departmentDetails.getName() + "' already exists");
            }
            department.setName(departmentDetails.getName());
        }

        // Check for duplicate code if changed
        if (departmentDetails.getCode() != null && !departmentDetails.getCode().equals(department.getCode())) {
            if (departmentRepository.existsByCode(departmentDetails.getCode())) {
                throw new DuplicateResourceException("Department with code '" + departmentDetails.getCode() + "' already exists");
            }
            department.setCode(departmentDetails.getCode());
        }

        if (departmentDetails.getDescription() != null) {
            department.setDescription(departmentDetails.getDescription());
        }

        return departmentRepository.save(department);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);
        departmentRepository.delete(department);
    }
}
