package com.example.student_management_system.repository;

import com.example.student_management_system.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);

    List<Enrollment> findByStudentIdAndAcademicYear(Long studentId, String academicYear);

    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId AND e.academicYear = :year")
    Optional<Enrollment> findByStudentAndCourseAndYear(
        @Param("studentId") Long studentId,
        @Param("courseId") Long courseId,
        @Param("year") String academicYear
    );

    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId AND e.academicYear = :year")
    List<Enrollment> findByCourseAndAcademicYear(@Param("courseId") Long courseId, @Param("year") String academicYear);

    Long countByStudentId(Long studentId);

    Long countByCourseId(Long courseId);

    boolean existsByStudentIdAndCourseIdAndAcademicYear(Long studentId, Long courseId, String academicYear);
}
