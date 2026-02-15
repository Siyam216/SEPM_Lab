package com.example.student_management_system.service;

import com.example.student_management_system.dto.TeacherRegistrationRequest;
import com.example.student_management_system.entity.Department;
import com.example.student_management_system.entity.Teacher;
import com.example.student_management_system.entity.User;
import com.example.student_management_system.exception.DuplicateResourceException;
import com.example.student_management_system.exception.ResourceNotFoundException;
import com.example.student_management_system.repository.DepartmentRepository;
import com.example.student_management_system.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public Teacher registerTeacher(TeacherRegistrationRequest request) {
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
        teacher.setPassword(request.getPassword()); // Plain text for now (no encryption)
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

        return teacherRepository.save(teacher);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
    }

    public List<Teacher> getTeachersByDepartment(Long departmentId) {
        return teacherRepository.findByDepartmentId(departmentId);
    }

    public List<Teacher> searchTeachers(String keyword) {
        return teacherRepository.searchTeachers(keyword);
    }

    @Transactional
    public Teacher updateTeacher(Long id, Teacher teacherDetails) {
        Teacher teacher = getTeacherById(id);

        if (teacherDetails.getName() != null) {
            teacher.setName(teacherDetails.getName());
        }
        if (teacherDetails.getPhone() != null) {
            teacher.setPhone(teacherDetails.getPhone());
        }
        if (teacherDetails.getSpecialization() != null) {
            teacher.setSpecialization(teacherDetails.getSpecialization());
        }
        if (teacherDetails.getQualification() != null) {
            teacher.setQualification(teacherDetails.getQualification());
        }
        if (teacherDetails.getExperience() != null) {
            teacher.setExperience(teacherDetails.getExperience());
        }
        if (teacherDetails.getOfficeRoom() != null) {
            teacher.setOfficeRoom(teacherDetails.getOfficeRoom());
        }
        if (teacherDetails.getDepartment() != null) {
            Department department = departmentRepository.findById(teacherDetails.getDepartment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            teacher.setDepartment(department);
        }

        return teacherRepository.save(teacher);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        Teacher teacher = getTeacherById(id);
        teacherRepository.delete(teacher);
    }

    public Long getTeacherCountByDepartment(Long departmentId) {
        return teacherRepository.countByDepartmentId(departmentId);
    }
}
