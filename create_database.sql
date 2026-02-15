-- SQL Script to Create Database for Student Management System
-- Run this script in PostgreSQL

-- Step 1: Create the database
CREATE DATABASE student_management_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Step 2: Connect to the database
\c student_management_db;

-- Step 3: Verify connection
SELECT 'Database created successfully!' as status;

-- You don't need to create tables manually
-- Spring Boot will create them automatically when you run the application
-- with spring.jpa.hibernate.ddl-auto=update

-- To verify after running the Spring Boot app, run:
-- \dt
