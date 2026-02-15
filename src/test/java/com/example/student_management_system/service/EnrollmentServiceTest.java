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
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment;
    private Student student;
    private Course course;
    private EnrollmentRequest enrollmentRequest;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("John Doe");

        course = new Course();
        course.setId(1L);
        course.setName("Data Structures");
        course.setCourseCode("CS101");

        enrollment = Enrollment.builder()
                .id(1L)
                .student(student)
                .course(course)
                .academicYear("2024-2025")
                .semester(1)
                .status(Enrollment.EnrollmentStatus.ENROLLED)
                .build();

        enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setStudentId(1L);
        enrollmentRequest.setCourseId(1L);
        enrollmentRequest.setAcademicYear("2024-2025");
        enrollmentRequest.setSemester(1);
    }

    @Test
    void testGetAllEnrollments() {
        when(enrollmentRepository.findAll()).thenReturn(Arrays.asList(enrollment));

        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

        assertThat(enrollments).hasSize(1);
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetEnrollmentById_Success() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        Enrollment found = enrollmentService.getEnrollmentById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEnrollmentById_NotFound() {
        when(enrollmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.getEnrollmentById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Enrollment not found");
    }

    @Test
    void testGetEnrollmentsByStudent() {
        when(enrollmentRepository.findByStudentId(1L)).thenReturn(Arrays.asList(enrollment));

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(1L);

        assertThat(enrollments).hasSize(1);
        verify(enrollmentRepository, times(1)).findByStudentId(1L);
    }

    @Test
    void testGetEnrollmentsByCourse() {
        when(enrollmentRepository.findByCourseId(1L)).thenReturn(Arrays.asList(enrollment));

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(1L);

        assertThat(enrollments).hasSize(1);
        verify(enrollmentRepository, times(1)).findByCourseId(1L);
    }

    @Test
    void testEnrollStudent_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseIdAndAcademicYear(anyLong(), anyLong(), anyString())).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment created = enrollmentService.enrollStudent(enrollmentRequest);

        assertThat(created).isNotNull();
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void testEnrollStudent_StudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.enrollStudent(enrollmentRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student not found");
    }

    @Test
    void testEnrollStudent_CourseNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.enrollStudent(enrollmentRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Course not found");
    }

    @Test
    void testEnrollStudent_AlreadyEnrolled() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseIdAndAcademicYear(1L, 1L, "2024-2025")).thenReturn(true);

        assertThatThrownBy(() -> enrollmentService.enrollStudent(enrollmentRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already enrolled");
    }

    @Test
    void testUpdateGrade() {
        GradeUpdateRequest request = new GradeUpdateRequest();
        request.setGrade("A");
        request.setMarks(85.0);

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment updated = enrollmentService.updateGrade(1L, request);

        assertThat(updated).isNotNull();
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    void testDeleteEnrollment() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        doNothing().when(enrollmentRepository).delete(enrollment);

        enrollmentService.deleteEnrollment(1L);

        verify(enrollmentRepository, times(1)).delete(enrollment);
    }
}
