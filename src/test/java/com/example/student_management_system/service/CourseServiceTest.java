package com.example.student_management_system.service;

import com.example.student_management_system.entity.Course;
import com.example.student_management_system.entity.Department;
import com.example.student_management_system.entity.Teacher;
import com.example.student_management_system.entity.User;
import com.example.student_management_system.exception.DuplicateResourceException;
import com.example.student_management_system.exception.ResourceNotFoundException;
import com.example.student_management_system.repository.CourseRepository;
import com.example.student_management_system.repository.DepartmentRepository;
import com.example.student_management_system.repository.TeacherRepository;
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
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private Department department;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");
        department.setCode("CS");

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Dr. Alice");
        teacher.setEmail("alice@test.com");
        teacher.setEmployeeId("EMP001");
        teacher.setPhone("1234567890");
        teacher.setRole(User.Role.TEACHER);
        teacher.setStatus(User.AccountStatus.ACTIVE);
        teacher.setDepartment(department);

        course = new Course();
        course.setId(1L);
        course.setName("Data Structures");
        course.setCourseCode("CS101");
        course.setDescription("Learn data structures");
        course.setCredits(4);
        course.setSemester(3);
        course.setCourseType(Course.CourseType.CORE);
        course.setDepartment(department);
        course.setTeacher(teacher);
    }

    @Test
    void testGetAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course));

        List<Course> courses = courseService.getAllCourses();

        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getName()).isEqualTo("Data Structures");
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetCourseById_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course found = courseService.getCourseById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Data Structures");
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.getCourseById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Course not found");
    }

    @Test
    void testGetCourseByCourseCode_Success() {
        when(courseRepository.findByCourseCode("CS101")).thenReturn(Optional.of(course));

        Course found = courseService.getCourseByCourseCode("CS101");

        assertThat(found).isNotNull();
        assertThat(found.getCourseCode()).isEqualTo("CS101");
    }

    @Test
    void testCreateCourse_Success() {
        when(courseRepository.existsByCourseCode("CS101")).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course created = courseService.createCourse(course);

        assertThat(created).isNotNull();
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testCreateCourse_DuplicateCourseCode() {
        when(courseRepository.existsByCourseCode("CS101")).thenReturn(true);

        assertThatThrownBy(() -> courseService.createCourse(course))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Course with code");
    }

    @Test
    void testUpdateCourse_Success() {
        Course updateDetails = new Course();
        updateDetails.setName("Advanced Data Structures");
        updateDetails.setDescription("Advanced topics");
        updateDetails.setCredits(5);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course updated = courseService.updateCourse(1L, updateDetails);

        assertThat(updated).isNotNull();
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testDeleteCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(course);

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void testGetCoursesByDepartment() {
        when(courseRepository.findByDepartmentId(1L)).thenReturn(Arrays.asList(course));

        List<Course> courses = courseService.getCoursesByDepartment(1L);

        assertThat(courses).hasSize(1);
        verify(courseRepository, times(1)).findByDepartmentId(1L);
    }

    @Test
    void testGetCoursesByTeacher() {
        when(courseRepository.findByTeacherId(1L)).thenReturn(Arrays.asList(course));

        List<Course> courses = courseService.getCoursesByTeacher(1L);

        assertThat(courses).hasSize(1);
        verify(courseRepository, times(1)).findByTeacherId(1L);
    }

    @Test
    void testGetCoursesBySemester() {
        when(courseRepository.findBySemester(3)).thenReturn(Arrays.asList(course));

        List<Course> courses = courseService.getCoursesBySemester(3);

        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getSemester()).isEqualTo(3);
    }

    @Test
    void testGetUnassignedCourses() {
        Course unassigned = new Course();
        unassigned.setCourseCode("CS102");
        when(courseRepository.findByTeacherIsNull()).thenReturn(Arrays.asList(unassigned));

        List<Course> courses = courseService.getUnassignedCourses();

        assertThat(courses).hasSize(1);
        verify(courseRepository, times(1)).findByTeacherIsNull();
    }

    @Test
    void testAssignTeacherToCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course updated = courseService.assignTeacherToCourse(1L, 1L);

        assertThat(updated.getTeacher()).isNotNull();
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testSearchCourses() {
        when(courseRepository.searchCourses("Data")).thenReturn(Arrays.asList(course));

        List<Course> results = courseService.searchCourses("Data");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).contains("Data");
    }
}
