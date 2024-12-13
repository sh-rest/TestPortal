ALTER TABLE Exam 
ADD COLUMN duration INT DEFAULT 60,
ADD COLUMN start_time DATETIME,
ADD COLUMN end_time DATETIME;

-- Update existing records with default values
UPDATE Exam 
SET duration = 60,
    start_time = date,
    end_time = DATE_ADD(date, INTERVAL 60 MINUTE)
WHERE duration IS NULL; 

-- Add this if you need to modify timezone settings
SET time_zone = '+05:30';

-- Update the available exams query to use IST
CREATE OR REPLACE VIEW available_exams AS
SELECT * FROM Exam 
WHERE start_time <= CONVERT_TZ(NOW(), 'UTC', '+05:30')
AND end_time >= CONVERT_TZ(NOW(), 'UTC', '+05:30'); 