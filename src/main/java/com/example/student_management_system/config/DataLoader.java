package com.example.student_management_system.config;

import com.example.student_management_system.entity.*;
import com.example.student_management_system.repository.CourseRepository;
import com.example.student_management_system.repository.DepartmentRepository;
import com.example.student_management_system.repository.StudentRepository;
import com.example.student_management_system.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    // Set this to true to force reload data (clears existing data)
    private static final boolean FORCE_RELOAD = true;

    @Override
    public void run(String... args) throws Exception {
        if (FORCE_RELOAD) {
            log.info("FORCE_RELOAD is enabled - clearing existing data...");
            clearAllData();
            loadSampleData();
        } else if (departmentRepository.count() == 0) {
            loadSampleData();
        } else {
            log.info("Sample data already exists. Set FORCE_RELOAD=true to reload.");
        }
    }

    private void clearAllData() {
        log.info("Deleting all existing data...");
        courseRepository.deleteAll();
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
        departmentRepository.deleteAll();
        log.info("All data cleared successfully!");
    }

    // Creates sample departments, teachers, students, and courses with BCrypt passwords
    private void loadSampleData() {
        log.info("Loading sample data...");

        // Create Departments
        Department csDept = Department.builder()
                .name("Computer Science")
                .code("CS")
                .description("Department of Computer Science and Engineering")
                .build();

        Department eeDept = Department.builder()
                .name("Electrical Engineering")
                .code("EE")
                .description("Department of Electrical Engineering")
                .build();

        Department meDept = Department.builder()
                .name("Mechanical Engineering")
                .code("ME")
                .description("Department of Mechanical Engineering")
                .build();

        departmentRepository.save(csDept);
        departmentRepository.save(eeDept);
        departmentRepository.save(meDept);
        log.info("Departments created");

        // Create Teachers
        Teacher teacher1 = Teacher.builder()
                .name("Dr. John Smith")
                .email("john.smith@university.edu")
                .password(passwordEncoder.encode("teacher123"))
                .phone("1234567890")
                .employeeId("T001")
                .specialization("Artificial Intelligence")
                .qualification("Ph.D. in Computer Science")
                .experience(10)
                .officeRoom("CS-301")
                .department(csDept)
                .build();
        teacher1.setRole(User.Role.TEACHER);
        teacher1.setStatus(User.AccountStatus.ACTIVE);

        Teacher teacher2 = Teacher.builder()
                .name("Dr. Sarah Johnson")
                .email("sarah.johnson@university.edu")
                .password(passwordEncoder.encode("teacher123"))
                .phone("1234567891")
                .employeeId("T002")
                .specialization("Database Systems")
                .qualification("Ph.D. in Information Systems")
                .experience(8)
                .officeRoom("CS-302")
                .department(csDept)
                .build();
        teacher2.setRole(User.Role.TEACHER);
        teacher2.setStatus(User.AccountStatus.ACTIVE);

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        log.info("Teachers created");

        // Create Courses
        Course course1 = Course.builder()
                .name("Data Structures and Algorithms")
                .courseCode("CS101")
                .description("Introduction to fundamental data structures and algorithms")
                .credits(4)
                .semester(3)
                .courseType(Course.CourseType.CORE)
                .department(csDept)
                .teacher(teacher1)
                .build();

        Course course2 = Course.builder()
                .name("Database Management Systems")
                .courseCode("CS201")
                .description("Relational databases, SQL, and database design")
                .credits(4)
                .semester(4)
                .courseType(Course.CourseType.CORE)
                .department(csDept)
                .teacher(teacher2)
                .build();

        Course course3 = Course.builder()
                .name("Machine Learning")
                .courseCode("CS301")
                .description("Introduction to machine learning algorithms and applications")
                .credits(3)
                .semester(6)
                .courseType(Course.CourseType.ELECTIVE)
                .department(csDept)
                .teacher(teacher1)
                .build();

        Course course4 = Course.builder()
                .name("Web Development Lab")
                .courseCode("CS102L")
                .description("Hands-on web development with modern frameworks")
                .credits(2)
                .semester(4)
                .courseType(Course.CourseType.LAB)
                .department(csDept)
                .teacher(teacher2)
                .build();

        // Create Courses WITHOUT assigned teachers
        Course course5 = Course.builder()
                .name("Operating Systems")
                .courseCode("CS202")
                .description("Process management, memory management, and file systems")
                .credits(4)
                .semester(5)
                .courseType(Course.CourseType.CORE)
                .department(csDept)
                .teacher(null) // No teacher assigned
                .build();

        Course course6 = Course.builder()
                .name("Computer Networks")
                .courseCode("CS203")
                .description("Network protocols, architecture, and security")
                .credits(3)
                .semester(5)
                .courseType(Course.CourseType.CORE)
                .department(csDept)
                .teacher(null) // No teacher assigned
                .build();

        Course course7 = Course.builder()
                .name("Software Engineering")
                .courseCode("CS302")
                .description("Software development lifecycle, testing, and project management")
                .credits(3)
                .semester(6)
                .courseType(Course.CourseType.CORE)
                .department(csDept)
                .teacher(null) // No teacher assigned
                .build();

        Course course8 = Course.builder()
                .name("Cloud Computing")
                .courseCode("CS401")
                .description("Cloud architecture, services, and deployment models")
                .credits(3)
                .semester(7)
                .courseType(Course.CourseType.ELECTIVE)
                .department(csDept)
                .teacher(null) // No teacher assigned
                .build();

        Course course9 = Course.builder()
                .name("Artificial Intelligence Lab")
                .courseCode("CS301L")
                .description("Practical AI implementations and projects")
                .credits(2)
                .semester(6)
                .courseType(Course.CourseType.LAB)
                .department(csDept)
                .teacher(null) // No teacher assigned
                .build();

        Course course10 = Course.builder()
                .name("Cybersecurity Fundamentals")
                .courseCode("CS303")
                .description("Security principles, cryptography, and threat analysis")
                .credits(3)
                .semester(6)
                .courseType(Course.CourseType.ELECTIVE)
                .department(csDept)
                .teacher(null) // No teacher assigned
                .build();

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);
        courseRepository.save(course4);
        courseRepository.save(course5);
        courseRepository.save(course6);
        courseRepository.save(course7);
        courseRepository.save(course8);
        courseRepository.save(course9);
        courseRepository.save(course10);
        log.info("Courses created (including 6 unassigned courses for teacher assignment)");

        // Create Sample Student (Active)
        Student student1 = Student.builder()
                .name("Alice Brown")
                .rollNumber("CS2024001")
                .phone("9876543210")
                .address("123 Main Street, City")
                .dateOfBirth("2004-05-15")
                .gender(Student.Gender.FEMALE)
                .semester(4)
                .guardianName("Robert Brown")
                .guardianContact("9876543211")
                .department(csDept)
                .build();
        student1.setEmail("alice.brown@student.edu");
        student1.setPassword(passwordEncoder.encode("student123"));
        student1.setRole(User.Role.STUDENT);
        student1.setStatus(User.AccountStatus.ACTIVE);

        // Create Sample Student (Pending Approval)
        Student student2 = Student.builder()
                .name("Bob Wilson")
                .rollNumber("CS2024002")
                .phone("9876543212")
                .address("456 Oak Avenue, City")
                .dateOfBirth("2004-08-22")
                .gender(Student.Gender.MALE)
                .semester(4)
                .guardianName("James Wilson")
                .guardianContact("9876543213")
                .department(csDept)
                .build();
        student2.setEmail("bob.wilson@student.edu");
        student2.setPassword(passwordEncoder.encode("student123"));
        student2.setRole(User.Role.STUDENT);
        student2.setStatus(User.AccountStatus.PENDING);

        studentRepository.save(student1);
        studentRepository.save(student2);
        log.info("Students created");

        log.info("Sample data loaded successfully!");
        log.info("===========================================");
        log.info("LOGIN CREDENTIALS:");
        log.info("Teacher Login: john.smith@university.edu / teacher123");
        log.info("Teacher Login: sarah.johnson@university.edu / teacher123");
        log.info("Student Login (Active): alice.brown@student.edu / student123");
        log.info("Student (Pending): bob.wilson@student.edu / student123");
        log.info("===========================================");
        log.info("COURSES INFO:");
        log.info("Total Courses: 10");
        log.info("Assigned Courses: 4 (already have teachers)");
        log.info("Unassigned Courses: 6 (available for teacher assignment)");
        log.info("===========================================");
    }
}
