package com.example.student_management_system.service;

import com.example.student_management_system.dto.EnrollmentRequest;
import com.example.student_management_system.dto.GradeUpdateRequest;
import com.example.student_management_system.entity.Course;
import com.example.student_management_system.entity.Enrollment;
import com.example.student_management_system.entity.Student;
import com.example.student_management_system.exception.DuplicateResourceException;
import com.example.student_management_system.exception.ResourceNotFoundException;
import com.example.student_management_system.repository.CourseRepository;
import com.example.student_management_system.repository.EnrollmentRepository;
import com.example.student_management_system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
    }

    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public List<Enrollment> getEnrollmentsByCourseAndYear(Long courseId, String academicYear) {
        return enrollmentRepository.findByCourseAndAcademicYear(courseId, academicYear);
    }

    @Transactional
    public Enrollment enrollStudent(EnrollmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // Check if already enrolled
        if (enrollmentRepository.existsByStudentIdAndCourseIdAndAcademicYear(
                request.getStudentId(), request.getCourseId(), request.getAcademicYear())) {
            throw new DuplicateResourceException("Student is already enrolled in this course for the academic year");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .academicYear(request.getAcademicYear())
                .semester(request.getSemester())
                .status(Enrollment.EnrollmentStatus.ENROLLED)
                .build();

        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public Enrollment updateGrade(Long enrollmentId, GradeUpdateRequest request) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);

        if (request.getGrade() != null) {
            enrollment.setGrade(request.getGrade());
        }
        if (request.getMarks() != null) {
            enrollment.setMarks(request.getMarks());
        }
        if (request.getStatus() != null) {
            enrollment.setStatus(Enrollment.EnrollmentStatus.valueOf(request.getStatus().toUpperCase()));
        }
        if (request.getRemarks() != null) {
            enrollment.setRemarks(request.getRemarks());
        }

        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void deleteEnrollment(Long id) {
        Enrollment enrollment = getEnrollmentById(id);
        enrollmentRepository.delete(enrollment);
    }

    public Long getEnrollmentCountByStudent(Long studentId) {
        return enrollmentRepository.countByStudentId(studentId);
    }

    public Long getEnrollmentCountByCourse(Long courseId) {
        return enrollmentRepository.countByCourseId(courseId);
    }
}
