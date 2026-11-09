-- Run manually in MySQL as an administrator, then run the application.
CREATE DATABASE IF NOT EXISTS roomdb;
CREATE USER IF NOT EXISTS 'roomapp'@'localhost' IDENTIFIED BY 'roomapp1234';
GRANT ALL PRIVILEGES ON roomdb.* TO 'roomapp'@'localhost';
USE roomdb;
CREATE TABLE IF NOT EXISTS room (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(255) NOT NULL,
 location VARCHAR(255),
 capacity INT NOT NULL CHECK (capacity BETWEEN 1 AND 20)
);
CREATE TABLE IF NOT EXISTS reservation (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 room_id BIGINT NOT NULL,
 reserved_by VARCHAR(255) NOT NULL,
 start_time DATETIME(6) NOT NULL,
 end_time DATETIME(6) NOT NULL,
 FOREIGN KEY (room_id) REFERENCES room(id),
 CHECK (end_time > start_time)
);
