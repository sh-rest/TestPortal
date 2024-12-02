-- teacher queries
USE TestPortal;

DELIMITER //

CREATE PROCEDURE CreateExam(
    IN course_id INT,
    IN exam_title VARCHAR(100),
    IN exam_description TEXT,
    IN exam_date DATETIME
)
BEGIN
    -- Insert the exam details into the exams table
    INSERT INTO Exam (title, description, date, course_id)
    VALUES (exam_title, exam_description, exam_date, course_id);
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE UpdateExam(
    IN exam_id INT,
    IN new_exam_title VARCHAR(100),
    IN new_exam_description TEXT,
    IN new_exam_date DATETIME
)
BEGIN
    UPDATE Exam
    SET title = new_exam_title,
        description = new_exam_description,
        date = new_exam_date
    WHERE exam_id = exam_id;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE DeleteExam(
    IN exam_id INT
)
BEGIN
    DELETE FROM Exam
    WHERE exam_id = exam_id;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE ViewExamsByTeacher(
    IN teacher_id INT
)
BEGIN
    SELECT e.exam_id, e.title AS exam_title, e.date AS exam_date, c.course_name
    FROM Exam e
    JOIN Course c ON e.course_id = c.course_id
    WHERE c.teacher_id = teacher_id;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE ViewQuestionsInExam(
    IN exam_id INT
)
BEGIN
    SELECT q.question_id, q.content AS question_text, q.type, q.options, q.correct_answer
    FROM Question q
    WHERE q.exam_id = exam_id;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE AddQuestionToExam(
    IN exam_id INT,
    IN question_content TEXT,
    IN question_type ENUM('MCQ', 'ShortAnswer'),
    IN options TEXT,  -- JSON-encoded options for MCQs
    IN correct_answer TEXT
)
BEGIN
    INSERT INTO Question (exam_id, content, type, options, correct_answer)
    VALUES (exam_id, question_content, question_type, options, correct_answer);
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE UpdateQuestionInExam(
    IN question_id INT,
    IN new_question_content TEXT,
    IN new_question_type ENUM('MCQ', 'ShortAnswer'),
    IN new_options TEXT,  -- JSON-encoded options for MCQs
    IN new_correct_answer TEXT
)
BEGIN
    UPDATE Question
    SET content = new_question_content,
        type = new_question_type,
        options = new_options,
        correct_answer = new_correct_answer
    WHERE question_id = question_id;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE DeleteQuestionFromExam(
    IN question_id INT
)
BEGIN
    DELETE FROM Question
    WHERE question_id = question_id;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE GetEnrolledStudents(
    IN teacher_id INT,
    IN course_id INT
)
BEGIN
    SELECT s.student_id, s.name AS student_name, s.email AS student_email
    FROM Student s
    JOIN StudentCourse sc ON s.student_id = sc.student_id
    WHERE sc.course_id = course_id
    AND EXISTS (
        SELECT 1
        FROM Course c
        WHERE c.course_id = course_id
        AND c.teacher_id = teacher_id
    );
END //

DELIMITER ;
--
DELIMITER //

CREATE PROCEDURE GetCoursesByTeacher(
    IN teacher_id INT
)
BEGIN
    SELECT c.course_id, c.course_name, c.description
    FROM Course c
    WHERE c.teacher_id = teacher_id;
END //

DELIMITER ;


