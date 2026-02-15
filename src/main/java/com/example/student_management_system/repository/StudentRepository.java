package com.example.student_management_system.repository;

import com.example.student_management_system.entity.Student;
import com.example.student_management_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    Optional<Student> findByRollNumber(String rollNumber);

    List<Student> findByStatus(User.AccountStatus status);

    List<Student> findByDepartmentId(Long departmentId);

    @Query("SELECT s FROM Student s WHERE s.department.id = :deptId AND s.status = :status")
    List<Student> findByDepartmentAndStatus(@Param("deptId") Long deptId, @Param("status") User.AccountStatus status);

    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Student> searchStudents(@Param("keyword") String keyword);

    boolean existsByRollNumber(String rollNumber);

    boolean existsByEmail(String email);

    Long countByDepartmentId(Long departmentId);
}
