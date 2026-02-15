package com.example.student_management_system.service;

import com.example.student_management_system.entity.Course;
import com.example.student_management_system.entity.Department;
import com.example.student_management_system.entity.Teacher;
import com.example.student_management_system.exception.DuplicateResourceException;
import com.example.student_management_system.exception.ResourceNotFoundException;
import com.example.student_management_system.repository.CourseRepository;
import com.example.student_management_system.repository.DepartmentRepository;
import com.example.student_management_system.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    public Course getCourseByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with code: " + courseCode));
    }

    public List<Course> getCoursesByDepartment(Long departmentId) {
        return courseRepository.findByDepartmentId(departmentId);
    }

    public List<Course> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    public List<Course> getCoursesBySemester(Integer semester) {
        return courseRepository.findBySemester(semester);
    }

    public List<Course> searchCourses(String keyword) {
        return courseRepository.searchCourses(keyword);
    }

    public List<Course> getUnassignedCourses() {
        return courseRepository.findByTeacherIsNull();
    }

    @Transactional
    public Course createCourse(Course course) {
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new DuplicateResourceException("Course with code '" + course.getCourseCode() + "' already exists");
        }

        // Validate department
        if (course.getDepartment() != null && course.getDepartment().getId() != null) {
            Department department = departmentRepository.findById(course.getDepartment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            course.setDepartment(department);
        }

        // Validate teacher
        if (course.getTeacher() != null && course.getTeacher().getId() != null) {
            Teacher teacher = teacherRepository.findById(course.getTeacher().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
            course.setTeacher(teacher);
        }

        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourse(Long id, Course courseDetails) {
        Course course = getCourseById(id);

        if (courseDetails.getName() != null) {
            course.setName(courseDetails.getName());
        }

        if (courseDetails.getCourseCode() != null && !courseDetails.getCourseCode().equals(course.getCourseCode())) {
            if (courseRepository.existsByCourseCode(courseDetails.getCourseCode())) {
                throw new DuplicateResourceException("Course with code '" + courseDetails.getCourseCode() + "' already exists");
            }
            course.setCourseCode(courseDetails.getCourseCode());
        }

        if (courseDetails.getDescription() != null) {
            course.setDescription(courseDetails.getDescription());
        }
        if (courseDetails.getCredits() != null) {
            course.setCredits(courseDetails.getCredits());
        }
        if (courseDetails.getSemester() != null) {
            course.setSemester(courseDetails.getSemester());
        }
        if (courseDetails.getCourseType() != null) {
            course.setCourseType(courseDetails.getCourseType());
        }

        if (courseDetails.getDepartment() != null && courseDetails.getDepartment().getId() != null) {
            Department department = departmentRepository.findById(courseDetails.getDepartment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            course.setDepartment(department);
        }

        if (courseDetails.getTeacher() != null && courseDetails.getTeacher().getId() != null) {
            Teacher teacher = teacherRepository.findById(courseDetails.getTeacher().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
            course.setTeacher(teacher);
        }

        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }

    @Transactional
    public Course assignTeacherToCourse(Long courseId, Long teacherId) {
        Course course = getCourseById(courseId);
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    public Long getCourseCountByDepartment(Long departmentId) {
        return courseRepository.countByDepartmentId(departmentId);
    }
}
