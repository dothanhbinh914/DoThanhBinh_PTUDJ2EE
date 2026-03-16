-- ================================================================
-- Course Registration System - Database: qlhocphan (Laragon MySQL)
-- ================================================================
-- Mở HeidiSQL trong Laragon hoặc chạy lệnh:
--   mysql -u root < init.sql
-- ================================================================

CREATE DATABASE IF NOT EXISTS qlhocphan
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE qlhocphan;

-- ----------------------------------------------------------------
-- DROP TABLES (thứ tự đúng tránh lỗi FK)
-- ----------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS enrollment;
DROP TABLE IF EXISTS student_role;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS role;
SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------------------------------------------
-- CREATE TABLES
-- ----------------------------------------------------------------

CREATE TABLE role (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE student (
    student_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE student_role (
    student_id BIGINT NOT NULL,
    role_id    BIGINT NOT NULL,
    PRIMARY KEY (student_id, role_id),
    CONSTRAINT fk_sr_student FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    CONSTRAINT fk_sr_role    FOREIGN KEY (role_id)    REFERENCES role(role_id)       ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE category (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE course (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    image       VARCHAR(255),
    credits     INT NOT NULL DEFAULT 3,
    lecturer    VARCHAR(150),
    category_id BIGINT,
    CONSTRAINT fk_course_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE enrollment (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id  BIGINT NOT NULL,
    course_id   BIGINT NOT NULL,
    enroll_date DATE,
    CONSTRAINT fk_enroll_student FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    CONSTRAINT fk_enroll_course  FOREIGN KEY (course_id)  REFERENCES course(id)          ON DELETE CASCADE,
    UNIQUE KEY uq_enrollment (student_id, course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------
-- INSERT: ROLES
-- ----------------------------------------------------------------
INSERT INTO role (name) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_STUDENT');

-- ----------------------------------------------------------------
-- INSERT: CATEGORIES
-- ----------------------------------------------------------------
INSERT INTO category (name) VALUES
    ('Công nghệ thông tin'),
    ('Kinh tế - Quản trị'),
    ('Ngoại ngữ'),
    ('Kỹ thuật'),
    ('Khoa học tự nhiên');

-- ----------------------------------------------------------------
-- INSERT: COURSES (20 học phần mẫu)
-- ----------------------------------------------------------------
INSERT INTO course (name, credits, lecturer, category_id) VALUES
-- Công nghệ thông tin (id=1)
    ('Lập trình Java cơ bản',          3, 'Nguyễn Văn An',    1),
    ('Lập trình Web với Spring Boot',  3, 'Trần Thị Bình',    1),
    ('Cơ sở dữ liệu',                  3, 'Lê Văn Cường',     1),
    ('Mạng máy tính',                  2, 'Phạm Thị Dung',    1),
    ('Cấu trúc dữ liệu & Giải thuật',  3, 'Hoàng Văn Em',     1),
    ('Lập trình Python',               2, 'Nguyễn Thị Fương', 1),
    ('Phát triển ứng dụng Mobile',     3, 'Trần Văn Giang',   1),
-- Kinh tế - Quản trị (id=2)
    ('Kinh tế vi mô',                  3, 'Lê Thị Hoa',       2),
    ('Kinh tế vĩ mô',                  3, 'Phạm Văn Ích',     2),
    ('Quản trị doanh nghiệp',          3, 'Hoàng Thị Kim',    2),
    ('Kế toán cơ bản',                 2, 'Nguyễn Văn Long',  2),
    ('Marketing căn bản',              2, 'Trần Thị Mai',     2),
-- Ngoại ngữ (id=3)
    ('Tiếng Anh giao tiếp 1',          3, 'Lê Văn Nam',       3),
    ('Tiếng Anh giao tiếp 2',          3, 'Phạm Thị Oanh',    3),
    ('Tiếng Nhật cơ bản',              3, 'Hoàng Văn Phú',    3),
-- Kỹ thuật (id=4)
    ('Kỹ thuật điện tử',               3, 'Nguyễn Thị Quỳnh', 4),
    ('Kỹ thuật số',                    2, 'Trần Văn Rạng',    4),
-- Khoa học tự nhiên (id=5)
    ('Toán cao cấp A1',                4, 'Lê Thị Sương',     5),
    ('Vật lý đại cương',               3, 'Phạm Văn Tú',      5),
    ('Xác suất thống kê',              3, 'Hoàng Thị Uyên',   5);

-- ----------------------------------------------------------------
-- NOTE: Tài khoản admin & student1 được tạo tự động bởi
-- DataInitializer khi ứng dụng Spring Boot khởi động lần đầu.
--   admin    / admin123   [ROLE_ADMIN]
--   student1 / student123 [ROLE_STUDENT]
-- ----------------------------------------------------------------

-- Xác nhận kết quả
SELECT CONCAT('Roles: ', COUNT(*))    AS result FROM role;
SELECT CONCAT('Categories: ', COUNT(*)) AS result FROM category;
SELECT CONCAT('Courses: ', COUNT(*))  AS result FROM course;
