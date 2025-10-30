-- Create database if not exists
CREATE DATABASE IF NOT EXISTS complaint_db;
USE complaint_db;

-- Create the 'roles' table with three roles
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Insert roles: Admin, Support Agent, and Customer
INSERT INTO roles (name) VALUES ('Admin'), ('Support Agent'), ('Customer');

-- Create the 'users' table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(200),
    role_id INT,
    is_active BOOLEAN NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Insert users (examples for each role)
INSERT INTO users (username, email, password_hash, full_name, role_id, is_active)
VALUES 
('admin_1', 'admin1@example.com', '$2a$12$tPK0iz.DIiSYbVEnQ0Ro9.QeZc8bGOezsF88gHvANnH2z9aCbQ4TO', 'Admin One', 1, TRUE),
('admin_2', 'admin2@example.com', '$2a$12$dpU5X/Uu/WOdDGDyCt0cgefpqhTPDDZQUBhH2tURpZNxWEUYNvgmW', 'Admin Two', 1, TRUE),
('support_1', 'support1@example.com', '$2a$12$361w/xXsnlNhcCyOTEHilu8hyg8zW2cdJOY6Oy1EjLxLATcH5WAT.', 'Support Agent One', 2, TRUE),
('support_2', 'support2@example.com', '$2a$12$wNvLXBoc25EVv4qDUVccfemnzqLR8le0sNfTAiKxAJHAOAuDtjcga', 'Support Agent Two', 2, TRUE),
('customer_1', 'customer1@example.com', '$2a$12$1qBOZfxZawUYSCK5/j8dMu6HZswdJixDgnFqpQOXRmvuRqLY/yuE6', 'Customer One', 3, TRUE),
('customer_2', 'customer2@example.com', '$2a$12$b9mbIMfFTDgmHvoVmkHPxeqmF0/7080TI03qOqtQNwgfkBaxipuJm', 'Customer Two', 3, TRUE),
('customer_3', 'customer3@example.com', '$2a$12$MU7q1mpQN.5jYhkVOJ903.YFQcOVSK8t0N0jpkEtGb2ROYdU3SiMW', 'Customer Three', 3, TRUE);

-- Create the 'tickets' table
CREATE TABLE tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reference VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    customer_id INT NOT NULL,
    assignee_id INT,
    sla_due_at TIMESTAMP NULL DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL DEFAULT NULL,
    resolved_at TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (assignee_id) REFERENCES users(id)
);

-- Insert tickets
INSERT INTO tickets (reference, title, description, status, priority, customer_id, assignee_id)
VALUES 
('TICKET-001', 'Network Issue', 'Customer reported network issue', 'Open', 'High', 5, 2),
('TICKET-002', 'Login Issue', 'Customer cannot log in', 'In Progress', 'Medium', 6, 4),
('TICKET-003', 'Billing Query', 'Customer has a query regarding the bill', 'Closed', 'Low', 7, 3);

-- Create the 'ticket_status_history' table
CREATE TABLE ticket_status_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    old_status VARCHAR(50),
    new_status VARCHAR(50),
    changed_by INT,
    comment TEXT,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id),
    FOREIGN KEY (changed_by) REFERENCES users(id)
);

-- Insert ticket status history
INSERT INTO ticket_status_history (ticket_id, old_status, new_status, changed_by, comment)
VALUES 
(1, 'Open', 'In Progress', 2, 'Started working on the issue'),
(2, 'In Progress', 'Resolved', 4, 'Fixed the login issue'),
(3, 'Closed', 'Closed', 3, 'Customer was satisfied with the resolution');

-- Create the 'assignments' table
CREATE TABLE assignments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    assigned_to INT NOT NULL,
    assigned_by INT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    note TEXT,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id),
    FOREIGN KEY (assigned_to) REFERENCES users(id),
    FOREIGN KEY (assigned_by) REFERENCES users(id)
);

-- Insert assignments
INSERT INTO assignments (ticket_id, assigned_to, assigned_by, note)
VALUES 
(1, 2, 1, 'Assigned to Admin Two for resolution'),
(2, 4, 3, 'Assigned to Support Agent Two for troubleshooting'),
(3, 3, 2, 'Assigned to Support Agent One for billing clarification');

-- Create the 'attachments' table
CREATE TABLE attachments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    uploaded_by INT,
    file_path VARCHAR(1024) NOT NULL,
    filename VARCHAR(255),
    content_type VARCHAR(100),
    size_bytes BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id),
    FOREIGN KEY (uploaded_by) REFERENCES users(id)
);

-- Insert attachments
INSERT INTO attachments (ticket_id, uploaded_by, file_path, filename, content_type, size_bytes)
VALUES 
(1, 2, '/files/issue_screenshot.jpg', 'issue_screenshot.jpg', 'image/jpeg', 5120),
(2, 4, '/files/login_error.png', 'login_error.png', 'image/png', 2048),
(3, 3, '/files/billing_query.pdf', 'billing_query.pdf', 'application/pdf', 10240);

-- Create the 'comments' table
CREATE TABLE comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    author_id INT NOT NULL,
    body TEXT,
    is_internal BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    edited_at TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id),
    FOREIGN KEY (author_id) REFERENCES users(id)
);

-- Insert comments
INSERT INTO comments (ticket_id, author_id, body, is_internal)
VALUES 
(1, 2, 'We are investigating the network issue', TRUE),
(2, 4, 'Login issue has been identified and fixed', FALSE),
(3, 3, 'The billing query has been resolved. Please check the updated statement.', FALSE);

-- Create the 'notifications' table
CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    ticket_id INT,
    title VARCHAR(255),
    body TEXT,
    is_read BOOLEAN NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (ticket_id) REFERENCES tickets(id)
);

-- Insert notifications
INSERT INTO notifications (user_id, ticket_id, title, body)
VALUES 
(5, 1, 'Ticket Update', 'Your ticket TICKET-001 is being worked on by Admin Two'),
(6, 2, 'Ticket Resolved', 'Your login issue has been resolved'),
(7, 3, 'Ticket Closed', 'Your billing query has been successfully addressed');