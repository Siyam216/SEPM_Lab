package com.example.student_management_system.repository;

import com.example.student_management_system.entity.Department;
import com.example.student_management_system.entity.Student;
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
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department;
    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        // Create department
        department = new Department();
        department.setName("Computer Science");
        department.setCode("CS");
        department.setDescription("Computer Science Department");
        department = departmentRepository.save(department);

        // Create students
        student1 = new Student();
        student1.setName("John Doe");
        student1.setEmail("john@test.com");
        student1.setPassword("password123");
        student1.setRollNumber("CS001");
        student1.setPhone("1234567890");
        student1.setRole(User.Role.STUDENT);
        student1.setStatus(User.AccountStatus.ACTIVE);
        student1.setDepartment(department);
        student1.setSemester(3);
        student1.setDateOfBirth("2000-01-01");
        student1 = studentRepository.save(student1);

        student2 = new Student();
        student2.setName("Jane Smith");
        student2.setEmail("jane@test.com");
        student2.setPassword("password123");
        student2.setRollNumber("CS002");
        student2.setPhone("0987654321");
        student2.setRole(User.Role.STUDENT);
        student2.setStatus(User.AccountStatus.PENDING);
        student2.setDepartment(department);
        student2.setSemester(2);
        student2.setDateOfBirth("2001-05-15");
        student2 = studentRepository.save(student2);
    }

    @Test
    void testFindByEmail() {
        Optional<Student> found = studentRepository.findByEmail("john@test.com");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void testFindByRollNumber() {
        Optional<Student> found = studentRepository.findByRollNumber("CS001");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void testExistsByEmail() {
        assertThat(studentRepository.existsByEmail("john@test.com")).isTrue();
        assertThat(studentRepository.existsByEmail("notfound@test.com")).isFalse();
    }

    @Test
    void testExistsByRollNumber() {
        assertThat(studentRepository.existsByRollNumber("CS001")).isTrue();
        assertThat(studentRepository.existsByRollNumber("CS999")).isFalse();
    }

    @Test
    void testFindByStatus() {
        List<Student> activeStudents = studentRepository.findByStatus(User.AccountStatus.ACTIVE);
        assertThat(activeStudents).hasSize(1);
        assertThat(activeStudents.get(0).getEmail()).isEqualTo("john@test.com");

        List<Student> pendingStudents = studentRepository.findByStatus(User.AccountStatus.PENDING);
        assertThat(pendingStudents).hasSize(1);
        assertThat(pendingStudents.get(0).getEmail()).isEqualTo("jane@test.com");
    }

    @Test
    void testFindByDepartmentId() {
        List<Student> students = studentRepository.findByDepartmentId(department.getId());
        assertThat(students).hasSize(2);
    }

    @Test
    void testSearchStudents() {
        List<Student> results = studentRepository.searchStudents("John");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("John Doe");

        List<Student> rollResults = studentRepository.searchStudents("CS001");
        assertThat(rollResults).hasSize(1);
        assertThat(rollResults.get(0).getRollNumber()).isEqualTo("CS001");
    }

    @Test
    void testCountByDepartmentId() {
        Long count = studentRepository.countByDepartmentId(department.getId());
        assertThat(count).isEqualTo(2);
    }
}
