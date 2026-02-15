package com.example.student_management_system.service;

import com.example.student_management_system.dto.StudentRegistrationRequest;
import com.example.student_management_system.dto.StudentUpdateRequest;
import com.example.student_management_system.entity.Department;
import com.example.student_management_system.entity.Student;
import com.example.student_management_system.entity.User;
import com.example.student_management_system.exception.DuplicateResourceException;
import com.example.student_management_system.exception.ResourceNotFoundException;
import com.example.student_management_system.exception.UnauthorizedException;
import com.example.student_management_system.repository.DepartmentRepository;
import com.example.student_management_system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public Student registerStudent(StudentRegistrationRequest request) {
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
        student.setPassword(request.getPassword()); // Plain text for now (no encryption)
        student.setPhone(request.getPhone());
        student.setRollNumber(request.getRollNumber());
        student.setRole(User.Role.STUDENT);
        student.setStatus(User.AccountStatus.PENDING); // Require teacher approval
        student.setAddress(request.getAddress());
        student.setDateOfBirth(request.getDateOfBirth());

        if (request.getGender() != null && !request.getGender().isEmpty()) {
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

        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public List<Student> getPendingStudents() {
        return studentRepository.findByStatus(User.AccountStatus.PENDING);
    }

    public List<Student> getStudentsByDepartment(Long departmentId) {
        return studentRepository.findByDepartmentId(departmentId);
    }

    public List<Student> searchStudents(String keyword) {
        return studentRepository.searchStudents(keyword);
    }

    @Transactional
    public Student updateStudent(Long id, StudentUpdateRequest request, String currentUserEmail) {
        Student student = getStudentById(id);

        // Check if the user is updating their own profile or is a teacher
        Student currentStudent = studentRepository.findByEmail(currentUserEmail).orElse(null);
        if (currentStudent != null && !currentStudent.getId().equals(id)) {
            throw new UnauthorizedException("You can only update your own profile");
        }

        // Update allowed fields (roll number and department cannot be changed by students)
        if (request.getName() != null) {
            student.setName(request.getName());
        }
        if (request.getPhone() != null) {
            student.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            student.setAddress(request.getAddress());
        }
        if (request.getDateOfBirth() != null) {
            student.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null && !request.getGender().isEmpty()) {
            student.setGender(Student.Gender.valueOf(request.getGender().toUpperCase()));
        }
        if (request.getSemester() != null) {
            student.setSemester(request.getSemester());
        }
        if (request.getGuardianName() != null) {
            student.setGuardianName(request.getGuardianName());
        }
        if (request.getGuardianContact() != null) {
            student.setGuardianContact(request.getGuardianContact());
        }

        // Department can only be updated by admin/teachers, not students themselves
        // Commenting out department update for student self-service
        // if (request.getDepartmentId() != null) {
        //     Department department = departmentRepository.findById(request.getDepartmentId())
        //             .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        //     student.setDepartment(department);
        // }

        return studentRepository.save(student);
    }

    @Transactional
    public Student approveStudent(Long id) {
        Student student = getStudentById(id);

        if (student.getStatus() != User.AccountStatus.PENDING) {
            throw new IllegalStateException("Student is not in pending status");
        }

        student.setStatus(User.AccountStatus.ACTIVE);
        return studentRepository.save(student);
    }

    @Transactional
    public Student rejectStudent(Long id) {
        Student student = getStudentById(id);

        if (student.getStatus() != User.AccountStatus.PENDING) {
            throw new IllegalStateException("Student is not in pending status");
        }

        student.setStatus(User.AccountStatus.REJECTED);
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    public Long getStudentCountByDepartment(Long departmentId) {
        return studentRepository.countByDepartmentId(departmentId);
    }
}
