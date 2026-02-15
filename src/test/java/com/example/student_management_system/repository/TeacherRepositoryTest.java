package com.example.student_management_system.repository;

import com.example.student_management_system.entity.Department;
import com.example.student_management_system.entity.Teacher;
import com.example.student_management_system.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department;
    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setName("Computer Science");
        department.setCode("CS");
        department.setDescription("CS Department");
        department = departmentRepository.save(department);

        teacher1 = new Teacher();
        teacher1.setName("Dr. Alice Smith");
        teacher1.setEmail("alice@test.com");
        teacher1.setPassword("password123");
        teacher1.setEmployeeId("EMP001");
        teacher1.setPhone("1234567890");
        teacher1.setRole(User.Role.TEACHER);
        teacher1.setStatus(User.AccountStatus.ACTIVE);
        teacher1.setDepartment(department);
        teacher1.setSpecialization("Algorithms");
        teacher1 = teacherRepository.save(teacher1);

        teacher2 = new Teacher();
        teacher2.setName("Prof. Bob Johnson");
        teacher2.setEmail("bob@test.com");
        teacher2.setPassword("password123");
        teacher2.setEmployeeId("EMP002");
        teacher2.setPhone("0987654321");
        teacher2.setRole(User.Role.TEACHER);
        teacher2.setStatus(User.AccountStatus.ACTIVE);
        teacher2.setDepartment(department);
        teacher2.setSpecialization("Database Systems");
        teacher2 = teacherRepository.save(teacher2);
    }

    @Test
    void testFindByEmail() {
        Optional<Teacher> found = teacherRepository.findByEmail("alice@test.com");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Dr. Alice Smith");
    }

    @Test
    void testFindByEmployeeId() {
        Optional<Teacher> found = teacherRepository.findByEmployeeId("EMP001");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Dr. Alice Smith");
    }

    @Test
    void testExistsByEmail() {
        assertThat(teacherRepository.existsByEmail("alice@test.com")).isTrue();
        assertThat(teacherRepository.existsByEmail("notfound@test.com")).isFalse();
    }

    @Test
    void testExistsByEmployeeId() {
        assertThat(teacherRepository.existsByEmployeeId("EMP001")).isTrue();
        assertThat(teacherRepository.existsByEmployeeId("EMP999")).isFalse();
    }

    @Test
    void testFindByDepartmentId() {
        List<Teacher> teachers = teacherRepository.findByDepartmentId(department.getId());
        assertThat(teachers).hasSize(2);
    }

    @Test
    void testSearchTeachers() {
        List<Teacher> results = teacherRepository.searchTeachers("Alice");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Dr. Alice Smith");

        List<Teacher> empResults = teacherRepository.searchTeachers("EMP001");
        assertThat(empResults).hasSize(1);
    }

    @Test
    void testCountByDepartmentId() {
        Long count = teacherRepository.countByDepartmentId(department.getId());
        assertThat(count).isEqualTo(2);
    }
}
