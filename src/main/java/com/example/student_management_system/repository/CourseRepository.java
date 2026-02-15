package com.example.student_management_system.repository;

import com.example.student_management_system.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseCode(String courseCode);

    List<Course> findByDepartmentId(Long departmentId);

    List<Course> findByTeacherId(Long teacherId);

    List<Course> findByTeacherIsNull();

    List<Course> findBySemester(Integer semester);

    @Query("SELECT c FROM Course c WHERE c.department.id = :deptId AND c.semester = :semester")
    List<Course> findByDepartmentAndSemester(@Param("deptId") Long deptId, @Param("semester") Integer semester);

    @Query("SELECT c FROM Course c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> searchCourses(@Param("keyword") String keyword);

    boolean existsByCourseCode(String courseCode);

    Long countByDepartmentId(Long departmentId);

    Long countByTeacherId(Long teacherId);
}
