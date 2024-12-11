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