-- Database setup for FutsalBook
-- Run this in MySQL (XAMPP phpMyAdmin) to manually create the database

CREATE DATABASE IF NOT EXISTS futsalbook_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE futsalbook_db;

-- Tables will be auto-created by JPA/Hibernate with spring.jpa.hibernate.ddl-auto=update
-- But if you want to create them manually, here's the structure:

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    registered_at DATETIME
);

-- Lapang table
CREATE TABLE IF NOT EXISTS lapang (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    photo_path VARCHAR(500)
);

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lapang_id BIGINT NOT NULL,
    lapang_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    created_at DATETIME
);

-- Insert sample data (optional)
INSERT INTO lapang (name) VALUES ('Lapang 1'), ('Lapang 2'), ('Lapang 3'), ('Lapang 4');
INSERT INTO users (username, password, role, registered_at) VALUES ('user1', 'user123', 'USER', NOW());
