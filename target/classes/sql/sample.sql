USE TestPortal;

INSERT INTO Admin (name, email, password)
VALUES ('Modi', 'admin@email.com', 'namorox');

INSERT INTO Teacher (name, email, password)
VALUES 
    ('Kushal Loya', 'kushal@teacher.com', 'kushal123'),
    ('Shresth Jain', 'shresth@teacher.com', 'shresth123');
    
INSERT INTO Student (name, email, password)
VALUES 
    ('Charlie Brown', 'charlie@example.com', 'charlie123'),
    ('Diana Prince', 'diana@example.com', 'diana123'),
    ('Ethan Hunt', 'ethan@example.com', 'ethan123');

INSERT INTO Course (course_name, description, teacher_id)
VALUES 
    ('CS101', 'DSA', 2), 
    ('EEE201', 'ADVD', 1);      

select * from Course;

INSERT INTO StudentCourse (student_id, course_id)
VALUES 
    (1, 1), -- Charlie Brown enrolled in DSA
    (2, 1), -- Diana Prince enrolled in DSA
    (3, 2), -- Ethan Hunt enrolled in ADVD
    (1, 2), -- Charlie Brown also enrolled in ADVD
    (2, 2); -- Diana Prince also enrolled in ADVD
    
INSERT INTO Exam (title, description, date, course_id)
VALUES 
    ('Midterm DSA', 'Covers algebra and geometry', '2024-12-15 10:00:00', 1), -- Connected to Mathematics 101
    ('ADVD Tut', 'Focus on mechanics', '2024-12-20 15:00:00', 2);                 -- Connected to Physics 201

INSERT INTO Question (exam_id, content, type, options, correct_answer)
VALUES 
    (1, 'What is 2 + 2?', 'MCQ', '[\"2\", \"3\", \"4\", \"5\"]', '4'), -- Question for DSA Midterm
    (2, 'Solve for x: 2x = 6', 'ShortAnswer', NULL, '3');            -- Question for ADVD Midterm
    
INSERT INTO Submission (student_id, exam_id, total_score, submission_date)
VALUES 
    (1, 1, 8.0, '2024-12-15 12:00:00'), -- Charlie submitted DSA Midterm
    (2, 2, 7.5, '2024-12-20 16:00:00'); -- Diana submitted ADVD Quiz

INSERT INTO Answer (submission_id, question_id, selected_option, is_correct)
VALUES 
    (1, 1, '4', TRUE),   -- Correct answer for Charlie
    (2, 2, '3', TRUE); -- Correct answer for Diana



