package com.example.student_management_system.repository;

import com.example.student_management_system.entity.Course;
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
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    private Department department;
    private Teacher teacher;
    private Course course1;
    private Course course2;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setName("Computer Science");
        department.setCode("CS");
        department = departmentRepository.save(department);

        teacher = new Teacher();
        teacher.setName("Dr. Alice");
        teacher.setEmail("alice@test.com");
        teacher.setPassword("pass");
        teacher.setEmployeeId("EMP001");
        teacher.setPhone("1234567890");
        teacher.setRole(User.Role.TEACHER);
        teacher.setStatus(User.AccountStatus.ACTIVE);
        teacher.setDepartment(department);
        teacher = teacherRepository.save(teacher);

        course1 = new Course();
        course1.setName("Data Structures");
        course1.setCourseCode("CS101");
        course1.setDescription("Learn data structures");
        course1.setCredits(4);
        course1.setSemester(3);
        course1.setCourseType(Course.CourseType.CORE);
        course1.setDepartment(department);
        course1.setTeacher(teacher);
        course1 = courseRepository.save(course1);

        course2 = new Course();
        course2.setName("Algorithms");
        course2.setCourseCode("CS102");
        course2.setDescription("Learn algorithms");
        course2.setCredits(3);
        course2.setSemester(4);
        course2.setCourseType(Course.CourseType.ELECTIVE);
        course2.setDepartment(department);
        course2 = courseRepository.save(course2);
    }

    @Test
    void testFindByCourseCode() {
        Optional<Course> found = courseRepository.findByCourseCode("CS101");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Data Structures");
    }

    @Test
    void testExistsByCourseCode() {
        assertThat(courseRepository.existsByCourseCode("CS101")).isTrue();
        assertThat(courseRepository.existsByCourseCode("CS999")).isFalse();
    }

    @Test
    void testFindByDepartmentId() {
        List<Course> courses = courseRepository.findByDepartmentId(department.getId());
        assertThat(courses).hasSize(2);
    }

    @Test
    void testFindByTeacherId() {
        List<Course> courses = courseRepository.findByTeacherId(teacher.getId());
        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getCourseCode()).isEqualTo("CS101");
    }

    @Test
    void testFindBySemester() {
        List<Course> courses = courseRepository.findBySemester(3);
        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getName()).isEqualTo("Data Structures");
    }

    @Test
    void testFindByTeacherIsNull() {
        List<Course> unassignedCourses = courseRepository.findByTeacherIsNull();
        assertThat(unassignedCourses).hasSize(1);
        assertThat(unassignedCourses.get(0).getCourseCode()).isEqualTo("CS102");
    }

    @Test
    void testSearchCourses() {
        List<Course> results = courseRepository.searchCourses("Data");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Data Structures");
    }

    @Test
    void testCountByDepartmentId() {
        Long count = courseRepository.countByDepartmentId(department.getId());
        assertThat(count).isEqualTo(2);
    }
}
