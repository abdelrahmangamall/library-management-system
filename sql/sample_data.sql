-- Create Database (if not exists)
CREATE DATABASE IF NOT EXISTS library_management;
USE library_management;


--Library Management System - Sample Data
--=======================================
--Default Test Accounts (Password: admin123):
--- admin / admin@library.com (ADMIN)
--- librarian1 / librarian1@library.com (LIBRARIAN)
--- staff1 / staff1@library.com (STAFF)
--
--Note: All passwords are hashed using BCrypt with strength 12


-- Insert Categories (Hierarchical)
-- Root Categories
INSERT INTO categories (name, description, level, display_order, is_active, parent_id, created_at, updated_at) VALUES
('Fiction', 'Fictional literature and novels', 0, 1, true, NULL, NOW(), NOW()),
('Non-Fiction', 'Non-fictional books and references', 0, 2, true, NULL, NOW(), NOW()),
('Science & Technology', 'Scientific and technical publications', 0, 3, true, NULL, NOW(), NOW()),
('Children & Young Adult', 'Books for children and teenagers', 0, 4, true, NULL, NOW(), NOW());

-- Sub-categories for Fiction
INSERT INTO categories (name, description, level, display_order, is_active, parent_id, created_at, updated_at) VALUES
('Mystery & Thriller', 'Mystery, thriller, and crime novels', 1, 1, true, 1, NOW(), NOW()),
('Romance', 'Romantic fiction', 1, 2, true, 1, NOW(), NOW()),
('Science Fiction', 'Science fiction novels', 1, 3, true, 1, NOW(), NOW()),
('Fantasy', 'Fantasy and magical realism', 1, 4, true, 1, NOW(), NOW()),
('Historical Fiction', 'Historical novels and period fiction', 1, 5, true, 1, NOW(), NOW());

-- Sub-categories for Non-Fiction
INSERT INTO categories (name, description, level, display_order, is_active, parent_id, created_at, updated_at) VALUES
('Biography & Autobiography', 'Life stories and memoirs', 1, 1, true, 2, NOW(), NOW()),
('History', 'Historical accounts and analysis', 1, 2, true, 2, NOW(), NOW()),
('Philosophy', 'Philosophical works and thought', 1, 3, true, 2, NOW(), NOW()),
('Self-Help', 'Personal development and improvement', 1, 4, true, 2, NOW(), NOW());

-- Sub-categories for Science & Technology
INSERT INTO categories (name, description, level, display_order, is_active, parent_id, created_at, updated_at) VALUES
('Computer Science', 'Programming, algorithms, and computing', 1, 1, true, 3, NOW(), NOW()),
('Physics', 'Physics and related sciences', 1, 2, true, 3, NOW(), NOW()),
('Mathematics', 'Mathematical works and textbooks', 1, 3, true, 3, NOW(), NOW());

-- Insert Authors
INSERT INTO authors (first_name, last_name, biography, birth_date, death_date, created_at, updated_at) VALUES
('Agatha', 'Christie', 'British crime writer known for detective novels featuring Hercule Poirot and Miss Marple.', '1890-09-15', '1976-01-12', NOW(), NOW()),
('Stephen', 'King', 'American author of horror, supernatural fiction, suspense, and fantasy novels.', '1947-09-21', NULL, NOW(), NOW()),
('J.K.', 'Rowling', 'British author best known for the Harry Potter fantasy series.', '1965-07-31', NULL, NOW(), NOW()),
('George', 'Orwell', 'English novelist and essayist, journalist and critic.', '1903-06-25', '1950-01-21', NOW(), NOW()),
('Jane', 'Austen', 'English novelist known primarily for her six major novels.', '1775-12-16', '1817-07-18', NOW(), NOW()),
('Ernest', 'Hemingway', 'American novelist, short-story writer, and journalist.', '1899-07-21', '1961-07-02', NOW(), NOW()),
('Toni', 'Morrison', 'American novelist, essayist, editor, teacher and professor emeritus.', '1931-02-18', '2019-08-05', NOW(), NOW()),
('Harper', 'Lee', 'American novelist widely known for To Kill a Mockingbird.', '1926-04-28', '2016-02-19', NOW(), NOW()),
('Mark', 'Twain', 'American writer, humorist, entrepreneur, publisher, and lecturer.', '1835-11-30', '1910-04-21', NOW(), NOW()),
('Virginia', 'Woolf', 'English writer, considered one of the most important modernist authors.', '1882-01-25', '1941-03-28', NOW(), NOW());

-- Insert Books
INSERT INTO books (title, isbn, publication_year, edition, summary, language, page_count, cover_image_url, is_available, publisher_id, created_at, updated_at) VALUES
('Murder on the Orient Express', '9780062693662', 1934, '1st Edition', 'A classic Hercule Poirot mystery involving a murder on the famous train.', 'English', 256, NULL, true, 1, NOW(), NOW()),
('The Shining', '9780307743657', 1977, '1st Edition', 'A horror novel about a family isolated in a haunted hotel.', 'English', 659, NULL, true, 2, NOW(), NOW()),
('Harry Potter and the Philosopher''s Stone', '9780439708180', 1997, '1st Edition', 'The first book in the Harry Potter series about a young wizard.', 'English', 309, NULL, false, 1, NOW(), NOW()),
('1984', '9780451524935', 1949, 'Reprint Edition', 'A dystopian social science fiction novel about totalitarian control.', 'English', 328, NULL, true, 3, NOW(), NOW()),
('Pride and Prejudice', '9780141439518', 1813, 'Modern Edition', 'A romantic novel about manners, upbringing, morality, and marriage.', 'English', 432, NULL, true, 1, NOW(), NOW()),
('The Old Man and the Sea', '9780684801223', 1952, '1st Edition', 'A short novel about an aging fisherman and his greatest catch.', 'English', 127, NULL, true, 4, NOW(), NOW()),
('Beloved', '9781400033416', 1987, '1st Edition', 'A novel about the aftermath of slavery and its impact on families.', 'English', 324, NULL, true, 2, NOW(), NOW()),
('To Kill a Mockingbird', '9780061120084', 1960, '1st Edition', 'A novel about racial injustice and moral growth in the American South.', 'English', 376, NULL, false, 5, NOW(), NOW()),
('The Adventures of Tom Sawyer', '9780486400778', 1876, 'Dover Edition', 'A classic adventure novel about a mischievous boy in Missouri.', 'English', 224, NULL, true, 3, NOW(), NOW()),
('Mrs. Dalloway', '9780156628709', 1925, 'Harvest Edition', 'A modernist novel following a day in the life of Clarissa Dalloway.', 'English', 194, NULL, true, 4, NOW(), NOW());

-- Insert Book-Author relationships
INSERT INTO book_authors (book_id, author_id) VALUES
(1, 1), -- Murder on the Orient Express - Agatha Christie
(2, 2), -- The Shining - Stephen King
(3, 3), -- Harry Potter - J.K. Rowling
(4, 4), -- 1984 - George Orwell
(5, 5), -- Pride and Prejudice - Jane Austen
(6, 6), -- The Old Man and the Sea - Hemingway
(7, 7), -- Beloved - Toni Morrison
(8, 8), -- To Kill a Mockingbird - Harper Lee
(9, 9), -- Tom Sawyer - Mark Twain
(10, 10); -- Mrs. Dalloway - Virginia Woolf

-- Insert Book-Category relationships
INSERT INTO book_categories (book_id, category_id) VALUES
(1, 5), -- Murder on the Orient Express - Mystery & Thriller
(2, 5), -- The Shining - Mystery & Thriller
(3, 8), -- Harry Potter - Fantasy
(4, 7), -- 1984 - Science Fiction
(5, 6), -- Pride and Prejudice - Romance
(5, 9), -- Pride and Prejudice - Historical Fiction
(6, 1), -- The Old Man and the Sea - Fiction
(7, 1), -- Beloved - Fiction
(7, 11), -- Beloved - History
(8, 1), -- To Kill a Mockingbird - Fiction
(9, 4), -- Tom Sawyer - Children & Young Adult
(10, 1); -- Mrs. Dalloway - Fiction

-- Insert System Users
INSERT INTO users (username, password_hash, email, role, is_active, created_at) VALUES
('admin', '$2a$12$3TLTRREJvTRr5ph2.zd5su8pIP0XXKUbleymcYo1BHImO/2l47O1y', 'admin@library.com', 'ADMIN', true, NOW()), -- password: admin123
('librarian1', '$2a$12$3TLTRREJvTRr5ph2.zd5su8pIP0XXKUbleymcYo1BHImO/2l47O1y', 'librarian1@library.com', 'LIBRARIAN', true, NOW()), -- password: admin123
('staff1', '$2a$12$3TLTRREJvTRr5ph2.zd5su8pIP0XXKUbleymcYo1BHImO/2l47O1y', 'staff1@library.com', 'STAFF', true, NOW()); -- password: admin123

-- Insert Members
INSERT INTO members (first_name, last_name, email, phone, address, is_active, created_at) VALUES
('John', 'Doe', 'john.doe@email.com', '+1-555-0101', '123 Main St, Anytown, ST 12345', true, NOW()),
('Jane', 'Smith', 'jane.smith@email.com', '+1-555-0102', '456 Oak Ave, Somewhere, ST 67890', true, NOW()),
('Robert', 'Johnson', 'robert.johnson@email.com', '+1-555-0103', '789 Pine Rd, Nowhere, ST 11111', true, NOW()),
('Emily', 'Davis', 'emily.davis@email.com', '+1-555-0104', '321 Elm St, Everywhere, ST 22222', true, NOW()),
('Michael', 'Wilson', 'michael.wilson@email.com', '+1-555-0105', '654 Maple Dr, Anywhere, ST 33333', false, NOW());

-- Insert Borrow Records
INSERT INTO borrow_records (book_id, member_id, user_id, borrow_date, due_date, return_date, fine_amount, status) VALUES
-- Currently borrowed books
(3, 1, 2, NOW() - INTERVAL 10 DAY, DATE(NOW() - INTERVAL 10 DAY) + INTERVAL 14 DAY, NULL, 0.0, 'BORROWED'),
(8, 2, 2, NOW() - INTERVAL 5 DAY, DATE(NOW() - INTERVAL 5 DAY) + INTERVAL 14 DAY, NULL, 0.0, 'BORROWED'),

-- Returned books
(1, 1, 2, NOW() - INTERVAL 30 DAY, DATE(NOW() - INTERVAL 30 DAY) + INTERVAL 14 DAY, NOW() - INTERVAL 16 DAY, 0.0, 'RETURNED'),
(2, 3, 2, NOW() - INTERVAL 25 DAY, DATE(NOW() - INTERVAL 25 DAY) + INTERVAL 14 DAY, NOW() - INTERVAL 11 DAY, 0.0, 'RETURNED'),

-- Overdue book
(4, 4, 2, NOW() - INTERVAL 20 DAY, DATE(NOW() - INTERVAL 20 DAY) + INTERVAL 14 DAY, NULL, 6.0, 'OVERDUE');

-- Insert User Activities (sample audit log)
INSERT INTO user_activities (user_id, action, entity_type, entity_id, description, ip_address, user_agent, timestamp) VALUES
(1, 'LOGIN', 'User', 1, 'User logged into system', '192.168.1.100', 'Mozilla/5.0', NOW() - INTERVAL 2 HOUR),
(2, 'CREATE', 'Book', 1, 'Created book: Murder on the Orient Express', '192.168.1.101', 'Mozilla/5.0', NOW() - INTERVAL 1 HOUR),
(2, 'BORROW', 'BorrowRecord', 1, 'Book borrowed: Harry Potter and the Philosopher''s Stone', '192.168.1.101', 'Mozilla/5.0', NOW() - INTERVAL 30 MINUTE),
(1, 'UPDATE', 'Member', 5, 'Deactivated member: Michael Wilson', '192.168.1.100', 'Mozilla/5.0', NOW() - INTERVAL 15 MINUTE);

-- Verification Queries
-- Check data insertion
SELECT 'Publishers' as table_name, COUNT(*) as count FROM publishers
UNION ALL
SELECT 'Categories', COUNT(*) FROM categories
UNION ALL
SELECT 'Authors', COUNT(*) FROM authors
UNION ALL
SELECT 'Books', COUNT(*) FROM books
UNION ALL
SELECT 'Users', COUNT(*) FROM users
UNION ALL
SELECT 'Members', COUNT(*) FROM members
UNION ALL
SELECT 'Borrow Records', COUNT(*) FROM borrow_records
UNION ALL
SELECT 'User Activities', COUNT(*) FROM user_activities;