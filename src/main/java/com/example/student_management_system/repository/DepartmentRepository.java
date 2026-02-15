package com.example.student_management_system.repository;

import com.example.student_management_system.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);

    Optional<Department> findByCode(String code);

    @Query("SELECT d FROM Department d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(d.code) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Department> searchDepartments(@Param("keyword") String keyword);

    boolean existsByName(String name);

    boolean existsByCode(String code);
}
