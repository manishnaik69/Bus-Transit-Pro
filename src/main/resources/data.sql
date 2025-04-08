-- Insert roles if they don't exist
INSERT INTO roles (name, description) 
VALUES 
('ROLE_ADMIN', 'Administrator role with full system access'),
('ROLE_PASSENGER', 'Regular passenger role'),
('ROLE_DRIVER', 'Bus driver role'),
('ROLE_MAINTENANCE', 'Maintenance staff role')
ON DUPLICATE KEY UPDATE description = VALUES(description);

-- Insert admin user if not exists
-- Password is 'admin123' (bcrypt encoded)
INSERT INTO users (username, password, email, full_name, phone_number, address, role_id, active, created_at, updated_at)
SELECT 'admin', '$2a$10$7QXjSzBXeLBJM7Uyc5JsGOBf5aOlWE6E5J7onC5WUa7Nkh2rPJgmG', 'admin@busmanagement.com', 'System Administrator', '9999999999', 'Head Office', r.id, true, NOW(), NOW()
FROM roles r
WHERE r.name = 'ROLE_ADMIN'
AND NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin')
LIMIT 1;

-- Insert driver user if not exists
-- Password is 'driver123' (bcrypt encoded)
INSERT INTO users (username, password, email, full_name, phone_number, address, role_id, active, created_at, updated_at)
SELECT 'driver', '$2a$10$sCA5ybq4BeKGM.pNYB9PVuQT7mKuGnVGnr6WHAujyhM3O64hNdEze', 'driver@busmanagement.com', 'Test Driver', '8888888888', 'Driver Address', r.id, true, NOW(), NOW()
FROM roles r
WHERE r.name = 'ROLE_DRIVER'
AND NOT EXISTS (SELECT 1 FROM users WHERE username = 'driver')
LIMIT 1;

-- Insert passenger user if not exists
-- Password is 'passenger123' (bcrypt encoded)
INSERT INTO users (username, password, email, full_name, phone_number, address, role_id, active, created_at, updated_at)
SELECT 'passenger', '$2a$10$6R3acfJwNOcYdZANQXdkqO0o1Z8xf5CznjOFz1lF.z.4UMYf9TAce', 'passenger@example.com', 'Test Passenger', '7777777777', 'Passenger Address', r.id, true, NOW(), NOW()
FROM roles r
WHERE r.name = 'ROLE_PASSENGER'
AND NOT EXISTS (SELECT 1 FROM users WHERE username = 'passenger')
LIMIT 1;

-- Insert maintenance user if not exists
-- Password is 'maintenance123' (bcrypt encoded)
INSERT INTO users (username, password, email, full_name, phone_number, address, role_id, active, created_at, updated_at)
SELECT 'maintenance', '$2a$10$RXjzlEQGpePvJ2Rbs.1iDOvU2OtJ7KZ7JVtW5PXJYblMIdwW2DWTC', 'maintenance@busmanagement.com', 'Test Maintenance Staff', '6666666666', 'Maintenance Office', r.id, true, NOW(), NOW()
FROM roles r
WHERE r.name = 'ROLE_MAINTENANCE'
AND NOT EXISTS (SELECT 1 FROM users WHERE username = 'maintenance')
LIMIT 1;

-- Insert Cities
INSERT INTO cities (name, state, description, created_at, updated_at)
VALUES 
('Delhi', 'Delhi', 'Capital city of India', NOW(), NOW()),
('Mumbai', 'Maharashtra', 'Financial capital of India', NOW(), NOW()),
('Bangalore', 'Karnataka', 'IT hub of India', NOW(), NOW()),
('Chennai', 'Tamil Nadu', 'Gateway to South India', NOW(), NOW()),
('Kolkata', 'West Bengal', 'Cultural capital of India', NOW(), NOW()),
('Hyderabad', 'Telangana', 'City of Pearls', NOW(), NOW()),
('Ahmedabad', 'Gujarat', 'Manchester of India', NOW(), NOW()),
('Pune', 'Maharashtra', 'Oxford of the East', NOW(), NOW()),
('Jaipur', 'Rajasthan', 'Pink City', NOW(), NOW()),
('Lucknow', 'Uttar Pradesh', 'City of Nawabs', NOW(), NOW())
ON DUPLICATE KEY UPDATE description = VALUES(description), updated_at = NOW();

-- Insert sample buses
INSERT INTO buses (registration_number, model, capacity, type, status, created_at, updated_at)
VALUES 
('DL01AB1234', 'Volvo 9400', 40, 'AC', 'Active', NOW(), NOW()),
('MH02CD5678', 'Ashok Leyland Viking', 52, 'Non-AC', 'Active', NOW(), NOW()),
('KA03EF9012', 'Tata Marcopolo', 45, 'AC', 'Active', NOW(), NOW()),
('TN04GH3456', 'Mercedes-Benz OH 1625', 38, 'Sleeper', 'Active', NOW(), NOW()),
('WB05IJ7890', 'Eicher Skyline', 42, 'Semi-Sleeper', 'Active', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Insert sample routes
-- First, get city IDs
SET @delhi_id = (SELECT id FROM cities WHERE name = 'Delhi' LIMIT 1);
SET @mumbai_id = (SELECT id FROM cities WHERE name = 'Mumbai' LIMIT 1);
SET @bangalore_id = (SELECT id FROM cities WHERE name = 'Bangalore' LIMIT 1);
SET @chennai_id = (SELECT id FROM cities WHERE name = 'Chennai' LIMIT 1);
SET @kolkata_id = (SELECT id FROM cities WHERE name = 'Kolkata' LIMIT 1);
SET @hyderabad_id = (SELECT id FROM cities WHERE name = 'Hyderabad' LIMIT 1);

-- Now insert routes if they don't exist
INSERT INTO routes (source_id, destination_id, distance, duration, fare_amount, created_at, updated_at)
SELECT @delhi_id, @mumbai_id, 1400, 24, 1500, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE source_id = @delhi_id AND destination_id = @mumbai_id);

INSERT INTO routes (source_id, destination_id, distance, duration, fare_amount, created_at, updated_at)
SELECT @mumbai_id, @delhi_id, 1400, 24, 1500, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE source_id = @mumbai_id AND destination_id = @delhi_id);

INSERT INTO routes (source_id, destination_id, distance, duration, fare_amount, created_at, updated_at)
SELECT @delhi_id, @bangalore_id, 2150, 36, 2200, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE source_id = @delhi_id AND destination_id = @bangalore_id);

INSERT INTO routes (source_id, destination_id, distance, duration, fare_amount, created_at, updated_at)
SELECT @bangalore_id, @delhi_id, 2150, 36, 2200, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE source_id = @bangalore_id AND destination_id = @delhi_id);

INSERT INTO routes (source_id, destination_id, distance, duration, fare_amount, created_at, updated_at)
SELECT @mumbai_id, @bangalore_id, 980, 16, 1200, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE source_id = @mumbai_id AND destination_id = @bangalore_id);

INSERT INTO routes (source_id, destination_id, distance, duration, fare_amount, created_at, updated_at)
SELECT @bangalore_id, @mumbai_id, 980, 16, 1200, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE source_id = @bangalore_id AND destination_id = @mumbai_id);

INSERT INTO routes (source_id, destination_id, distance, duration, fare_amount, created_at, updated_at)
SELECT @bangalore_id, @chennai_id, 350, 6, 800, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE source_id = @bangalore_id AND destination_id = @chennai_id);

INSERT INTO routes (source_id, destination_id, distance, duration, fare_amount, created_at, updated_at)
SELECT @chennai_id, @bangalore_id, 350, 6, 800, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE source_id = @chennai_id AND destination_id = @bangalore_id);

INSERT INTO routes (source_id, destination_id, distance, duration, fare_amount, created_at, updated_at)
SELECT @delhi_id, @kolkata_id, 1500, 30, 1800, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE source_id = @delhi_id AND destination_id = @kolkata_id);

INSERT INTO routes (source_id, destination_id, distance, duration, fare_amount, created_at, updated_at)
SELECT @kolkata_id, @delhi_id, 1500, 30, 1800, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE source_id = @kolkata_id AND destination_id = @delhi_id);

INSERT INTO routes (source_id, destination_id, distance, duration, fare_amount, created_at, updated_at)
SELECT @hyderabad_id, @chennai_id, 630, 12, 1000, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE source_id = @hyderabad_id AND destination_id = @chennai_id);

INSERT INTO routes (source_id, destination_id, distance, duration, fare_amount, created_at, updated_at)
SELECT @chennai_id, @hyderabad_id, 630, 12, 1000, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE source_id = @chennai_id AND destination_id = @hyderabad_id);

-- Get driver ID
SET @driver_id = (SELECT id FROM users WHERE username = 'driver' LIMIT 1);

-- Get route IDs
SET @route_1_id = (SELECT id FROM routes WHERE source_id = @delhi_id AND destination_id = @mumbai_id LIMIT 1);
SET @route_2_id = (SELECT id FROM routes WHERE source_id = @mumbai_id AND destination_id = @bangalore_id LIMIT 1);
SET @route_3_id = (SELECT id FROM routes WHERE source_id = @bangalore_id AND destination_id = @chennai_id LIMIT 1);

-- Get bus IDs
SET @bus_1_id = (SELECT id FROM buses WHERE registration_number = 'DL01AB1234' LIMIT 1);
SET @bus_2_id = (SELECT id FROM buses WHERE registration_number = 'MH02CD5678' LIMIT 1);
SET @bus_3_id = (SELECT id FROM buses WHERE registration_number = 'KA03EF9012' LIMIT 1);

-- Insert sample schedules
-- Future dates
SET @tomorrow = DATE_ADD(CURDATE(), INTERVAL 1 DAY);
SET @day_after = DATE_ADD(CURDATE(), INTERVAL 2 DAY);
SET @next_week = DATE_ADD(CURDATE(), INTERVAL 7 DAY);

-- Create seat map JSON for 40-seat bus
SET @seat_map_40 = '{"1":"AVAILABLE","2":"AVAILABLE","3":"AVAILABLE","4":"AVAILABLE","5":"AVAILABLE","6":"AVAILABLE","7":"AVAILABLE","8":"AVAILABLE","9":"AVAILABLE","10":"AVAILABLE","11":"AVAILABLE","12":"AVAILABLE","13":"AVAILABLE","14":"AVAILABLE","15":"AVAILABLE","16":"AVAILABLE","17":"AVAILABLE","18":"AVAILABLE","19":"AVAILABLE","20":"AVAILABLE","21":"AVAILABLE","22":"AVAILABLE","23":"AVAILABLE","24":"AVAILABLE","25":"AVAILABLE","26":"AVAILABLE","27":"AVAILABLE","28":"AVAILABLE","29":"AVAILABLE","30":"AVAILABLE","31":"AVAILABLE","32":"AVAILABLE","33":"AVAILABLE","34":"AVAILABLE","35":"AVAILABLE","36":"AVAILABLE","37":"AVAILABLE","38":"AVAILABLE","39":"AVAILABLE","40":"AVAILABLE"}';

-- Create seat map JSON for 52-seat bus
SET @seat_map_52 = '{"1":"AVAILABLE","2":"AVAILABLE","3":"AVAILABLE","4":"AVAILABLE","5":"AVAILABLE","6":"AVAILABLE","7":"AVAILABLE","8":"AVAILABLE","9":"AVAILABLE","10":"AVAILABLE","11":"AVAILABLE","12":"AVAILABLE","13":"AVAILABLE","14":"AVAILABLE","15":"AVAILABLE","16":"AVAILABLE","17":"AVAILABLE","18":"AVAILABLE","19":"AVAILABLE","20":"AVAILABLE","21":"AVAILABLE","22":"AVAILABLE","23":"AVAILABLE","24":"AVAILABLE","25":"AVAILABLE","26":"AVAILABLE","27":"AVAILABLE","28":"AVAILABLE","29":"AVAILABLE","30":"AVAILABLE","31":"AVAILABLE","32":"AVAILABLE","33":"AVAILABLE","34":"AVAILABLE","35":"AVAILABLE","36":"AVAILABLE","37":"AVAILABLE","38":"AVAILABLE","39":"AVAILABLE","40":"AVAILABLE","41":"AVAILABLE","42":"AVAILABLE","43":"AVAILABLE","44":"AVAILABLE","45":"AVAILABLE","46":"AVAILABLE","47":"AVAILABLE","48":"AVAILABLE","49":"AVAILABLE","50":"AVAILABLE","51":"AVAILABLE","52":"AVAILABLE"}';

-- Create seat map JSON for 45-seat bus
SET @seat_map_45 = '{"1":"AVAILABLE","2":"AVAILABLE","3":"AVAILABLE","4":"AVAILABLE","5":"AVAILABLE","6":"AVAILABLE","7":"AVAILABLE","8":"AVAILABLE","9":"AVAILABLE","10":"AVAILABLE","11":"AVAILABLE","12":"AVAILABLE","13":"AVAILABLE","14":"AVAILABLE","15":"AVAILABLE","16":"AVAILABLE","17":"AVAILABLE","18":"AVAILABLE","19":"AVAILABLE","20":"AVAILABLE","21":"AVAILABLE","22":"AVAILABLE","23":"AVAILABLE","24":"AVAILABLE","25":"AVAILABLE","26":"AVAILABLE","27":"AVAILABLE","28":"AVAILABLE","29":"AVAILABLE","30":"AVAILABLE","31":"AVAILABLE","32":"AVAILABLE","33":"AVAILABLE","34":"AVAILABLE","35":"AVAILABLE","36":"AVAILABLE","37":"AVAILABLE","38":"AVAILABLE","39":"AVAILABLE","40":"AVAILABLE","41":"AVAILABLE","42":"AVAILABLE","43":"AVAILABLE","44":"AVAILABLE","45":"AVAILABLE"}';

-- Insert schedules if they don't exist
INSERT INTO schedules (route_id, bus_id, driver_id, departure_time, arrival_time, available_seats, seat_map, status, created_at, updated_at)
SELECT @route_1_id, @bus_1_id, @driver_id, 
       TIMESTAMP(CONCAT(@tomorrow, ' 08:00:00')), 
       TIMESTAMP(CONCAT(DATE_ADD(@tomorrow, INTERVAL 1 DAY), ' 08:00:00')), 
       40, @seat_map_40, 'Scheduled', NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM schedules 
    WHERE route_id = @route_1_id AND bus_id = @bus_1_id 
    AND departure_time = TIMESTAMP(CONCAT(@tomorrow, ' 08:00:00'))
);

INSERT INTO schedules (route_id, bus_id, driver_id, departure_time, arrival_time, available_seats, seat_map, status, created_at, updated_at)
SELECT @route_2_id, @bus_2_id, @driver_id, 
       TIMESTAMP(CONCAT(@day_after, ' 10:00:00')), 
       TIMESTAMP(CONCAT(DATE_ADD(@day_after, INTERVAL 1 DAY), ' 02:00:00')), 
       52, @seat_map_52, 'Scheduled', NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM schedules 
    WHERE route_id = @route_2_id AND bus_id = @bus_2_id 
    AND departure_time = TIMESTAMP(CONCAT(@day_after, ' 10:00:00'))
);

INSERT INTO schedules (route_id, bus_id, driver_id, departure_time, arrival_time, available_seats, seat_map, status, created_at, updated_at)
SELECT @route_3_id, @bus_3_id, @driver_id, 
       TIMESTAMP(CONCAT(@next_week, ' 14:00:00')), 
       TIMESTAMP(CONCAT(@next_week, ' 20:00:00')), 
       45, @seat_map_45, 'Scheduled', NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM schedules 
    WHERE route_id = @route_3_id AND bus_id = @bus_3_id 
    AND departure_time = TIMESTAMP(CONCAT(@next_week, ' 14:00:00'))
);
