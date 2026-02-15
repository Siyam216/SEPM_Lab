-- Quick Database Verification Script
-- Run this in pgAdmin Query Tool

-- 1. Check if you're connected to the right database
SELECT current_database();

-- 2. List all tables in the public schema
SELECT tablename
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY tablename;

-- 3. Count records in each table (if they exist)
SELECT
    'departments' as table_name,
    COUNT(*) as record_count
FROM departments
UNION ALL
SELECT 'users', COUNT(*) FROM users
UNION ALL
SELECT 'students', COUNT(*) FROM students
UNION ALL
SELECT 'teachers', COUNT(*) FROM teachers
UNION ALL
SELECT 'courses', COUNT(*) FROM courses
UNION ALL
SELECT 'enrollments', COUNT(*) FROM enrollments;

-- 4. View all students with their details
SELECT
    s.id,
    s.name,
    s.email,
    s.roll_number,
    s.semester,
    d.name as department_name,
    u.status
FROM students s
LEFT JOIN departments d ON s.department_id = d.id
LEFT JOIN users u ON s.id = u.id
ORDER BY s.id;

-- 5. View all teachers
SELECT
    t.id,
    t.name,
    t.email,
    t.employee_id,
    d.name as department_name
FROM teachers t
LEFT JOIN departments d ON t.department_id = d.id
ORDER BY t.id;

-- 6. View all courses
SELECT
    c.id,
    c.name,
    c.course_code,
    c.credits,
    c.semester,
    d.name as department_name,
    t.name as teacher_name
FROM courses c
LEFT JOIN departments d ON c.department_id = d.id
LEFT JOIN teachers t ON c.teacher_id = t.id
ORDER BY c.semester, c.course_code;
