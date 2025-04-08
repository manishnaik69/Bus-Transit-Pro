-- Insert Roles
INSERT INTO roles (name, description)
VALUES 
  ('ROLE_ADMIN', 'Administrator role with full system access'),
  ('ROLE_PASSENGER', 'Regular passenger role'),
  ('ROLE_DRIVER', 'Bus driver role'),
  ('ROLE_MAINTENANCE', 'Maintenance staff role')
ON CONFLICT (name) DO UPDATE SET description = EXCLUDED.description;

-- Insert Admin User
-- Password is 'admin123' (bcrypt encoded)
WITH role_id AS (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')
INSERT INTO users (username, password, email, full_name, phone_number, address, role_id, active, created_at, updated_at)
SELECT 'admin', '$2a$10$7QXjSzBXeLBJM7Uyc5JsGOBf5aOlWE6E5J7onC5WUa7Nkh2rPJgmG', 'admin@busmanagement.com', 'System Administrator', '9999999999', 'Head Office', r.id, true, NOW(), NOW()
FROM roles r
WHERE r.name = 'ROLE_ADMIN'
AND NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Insert Driver User
-- Password is 'driver123' (bcrypt encoded)
WITH role_id AS (SELECT id FROM roles WHERE name = 'ROLE_DRIVER')
INSERT INTO users (username, password, email, full_name, phone_number, address, role_id, active, created_at, updated_at)
SELECT 'driver', '$2a$10$sCA5ybq4BeKGM.pNYB9PVuQT7mKuGnVGnr6WHAujyhM3O64hNdEze', 'driver@busmanagement.com', 'Test Driver', '8888888888', 'Driver Address', r.id, true, NOW(), NOW()
FROM roles r
WHERE r.name = 'ROLE_DRIVER'
AND NOT EXISTS (SELECT 1 FROM users WHERE username = 'driver');

-- Insert Passenger User
-- Password is 'passenger123' (bcrypt encoded)
WITH role_id AS (SELECT id FROM roles WHERE name = 'ROLE_PASSENGER')
INSERT INTO users (username, password, email, full_name, phone_number, address, role_id, active, created_at, updated_at)
SELECT 'passenger', '$2a$10$sCA5ybq4BeKGM.pNYB9PVuQT7mKuGnVGnr6WHAujyhM3O64hNdEze', 'passenger@busmanagement.com', 'Test Passenger', '7777777777', 'Passenger Address', r.id, true, NOW(), NOW()
FROM roles r
WHERE r.name = 'ROLE_PASSENGER'
AND NOT EXISTS (SELECT 1 FROM users WHERE username = 'passenger');

-- Insert Maintenance User
-- Password is 'maintenance123' (bcrypt encoded)
WITH role_id AS (SELECT id FROM roles WHERE name = 'ROLE_MAINTENANCE')
INSERT INTO users (username, password, email, full_name, phone_number, address, role_id, active, created_at, updated_at)
SELECT 'maintenance', '$2a$10$sCA5ybq4BeKGM.pNYB9PVuQT7mKuGnVGnr6WHAujyhM3O64hNdEze', 'maintenance@busmanagement.com', 'Test Maintenance', '6666666666', 'Maintenance Address', r.id, true, NOW(), NOW()
FROM roles r
WHERE r.name = 'ROLE_MAINTENANCE'
AND NOT EXISTS (SELECT 1 FROM users WHERE username = 'maintenance');