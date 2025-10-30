-- Create database if not exists
CREATE DATABASE IF NOT EXISTS complaint_db;
USE complaint_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    role ENUM('ADMIN', 'SUPPORT_AGENT', 'CUSTOMER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create tickets table
CREATE TABLE IF NOT EXISTS tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reference VARCHAR(50) UNIQUE NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    priority ENUM('LOW', 'MEDIUM', 'HIGH') DEFAULT 'MEDIUM',
    status ENUM('OPEN', 'ASSIGNED', 'IN_PROGRESS', 'RESOLVED', 'CLOSED') DEFAULT 'OPEN',
    customer_id INT NOT NULL,
    assignee_id INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    sla_due_at TIMESTAMP NULL,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (assignee_id) REFERENCES users(id)
);

-- Create notifications table
CREATE TABLE IF NOT EXISTS notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    ticket_id INT NULL,
    title VARCHAR(200),
    body TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    sender_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (ticket_id) REFERENCES tickets(id)
);

-- Create assignments table
CREATE TABLE IF NOT EXISTS assignments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    assigned_to_id INT NOT NULL,
    assigned_by_id INT NOT NULL,
    note TEXT,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id),
    FOREIGN KEY (assigned_to_id) REFERENCES users(id),
    FOREIGN KEY (assigned_by_id) REFERENCES users(id)
);

-- Create ticket_status_history table
CREATE TABLE IF NOT EXISTS ticket_status_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    old_status VARCHAR(50),
    new_status VARCHAR(50) NOT NULL,
    changed_by_id INT NOT NULL,
    comment TEXT,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id),
    FOREIGN KEY (changed_by_id) REFERENCES users(id)
);

-- Insert sample users
INSERT IGNORE INTO users (id, username, password, full_name, email, role) VALUES
(1, 'admin1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFVMLkxNvuxuzkKIVpMpOGm', 'Admin User', 'admin@complaint.com', 'ADMIN'),
(2, 'support1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFVMLkxNvuxuzkKIVpMpOGm', 'John Smith', 'support1@complaint.com', 'SUPPORT_AGENT'),
(3, 'customer1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFVMLkxNvuxuzkKIVpMpOGm', 'Jane Doe', 'customer1@complaint.com', 'CUSTOMER'),
(4, 'support2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFVMLkxNvuxuzkKIVpMpOGm', 'Mike Wilson', 'support2@complaint.com', 'SUPPORT_AGENT'),
(5, 'customer2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFVMLkxNvuxuzkKIVpMpOGm', 'Sarah Johnson', 'customer2@complaint.com', 'CUSTOMER');

-- Insert sample tickets
INSERT IGNORE INTO tickets (id, reference, title, description, priority, status, customer_id, assignee_id) VALUES
(1, 'TCK-001', 'Login Issue', 'Cannot login to the system', 'HIGH', 'OPEN', 3, NULL),
(2, 'TCK-002', 'Password Reset', 'Need to reset my password', 'MEDIUM', 'ASSIGNED', 5, 2),
(3, 'TCK-003', 'Feature Request', 'Add dark mode to the application', 'LOW', 'IN_PROGRESS', 3, 4);

-- Insert sample notifications
INSERT IGNORE INTO notifications (id, user_id, ticket_id, title, body, is_read, sender_name) VALUES
(1, 3, 1, 'Welcome', 'Welcome to the complaint management system!', FALSE, 'Admin'),
(2, 3, 2, 'Ticket Updated', 'Your ticket has been assigned to a support agent', FALSE, 'System'),
(3, 5, 2, 'Password Reset', 'Your password reset request has been processed', TRUE, 'Support Agent');