package com.example.student_management_system.repository;

import com.example.student_management_system.entity.Teacher;
import com.example.student_management_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByEmail(String email);

    Optional<Teacher> findByEmployeeId(String employeeId);

    List<Teacher> findByStatus(User.AccountStatus status);

    List<Teacher> findByDepartmentId(Long departmentId);

    @Query("SELECT t FROM Teacher t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.employeeId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Teacher> searchTeachers(@Param("keyword") String keyword);

    boolean existsByEmployeeId(String employeeId);

    boolean existsByEmail(String email);

    Long countByDepartmentId(Long departmentId);
}
