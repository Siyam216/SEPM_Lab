-- Script to clear all data and reload sample data
-- Run this in pgAdmin before restarting your application

-- Disable foreign key checks temporarily
SET session_replication_role = 'replica';

-- Delete all data in correct order (to avoid foreign key issues)
DELETE FROM enrollments;
DELETE FROM courses;
DELETE FROM students;
DELETE FROM teachers;
DELETE FROM users;
DELETE FROM departments;

-- Re-enable foreign key checks
SET session_replication_role = 'origin';

-- Verify tables are empty
SELECT 'departments' as table_name, COUNT(*) as count FROM departments
UNION ALL
SELECT 'users', COUNT(*) FROM users
UNION ALL
SELECT 'teachers', COUNT(*) FROM teachers
UNION ALL
SELECT 'students', COUNT(*) FROM students
UNION ALL
SELECT 'courses', COUNT(*) FROM courses
UNION ALL
SELECT 'enrollments', COUNT(*) FROM enrollments;

-- After running this, restart your Spring Boot application
-- It will automatically reload all sample data including 6 unassigned courses
