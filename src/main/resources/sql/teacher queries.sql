-- teacher queries
USE TestPortal;

DELIMITER //

DROP PROCEDURE IF EXISTS CreateExam //

CREATE PROCEDURE CreateExam(
    IN examTitle VARCHAR(100),
    IN examDescription TEXT,
    IN examDate DATETIME,
    IN courseId INT
)
BEGIN
    DECLARE examIdOut INT;
    
    -- Check if course exists
    IF NOT EXISTS (SELECT 1 FROM Course WHERE course_id = courseId) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Course not found';
    END IF;
    
    -- Insert the exam
    INSERT INTO Exam (title, description, date, course_id)
    VALUES (examTitle, examDescription, examDate, courseId);
    
    -- Get the last inserted ID
    SET examIdOut = LAST_INSERT_ID();
    
    -- Return the exam ID
    SELECT examIdOut as exam_id;
END //

DELIMITER ;

DELIMITER //

DROP PROCEDURE IF EXISTS UpdateExam //

CREATE PROCEDURE UpdateExam(
    IN p_exam_id INT,
    IN p_title VARCHAR(100),
    IN p_description TEXT,
    IN p_date DATETIME,
    IN p_duration INT,
    IN p_start_time DATETIME,
    IN p_end_time DATETIME
)
BEGIN
    UPDATE Exam
    SET title = p_title,
        description = p_description,
        date = p_date,
        duration = p_duration,
        start_time = p_start_time,
        end_time = p_end_time
    WHERE exam_id = p_exam_id;
    
    -- Return number of rows affected
    SELECT ROW_COUNT() as updated_rows;
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

CREATE PROCEDURE AddQuestion(
    IN examId INT,
    IN questionContent TEXT,
    IN questionType ENUM('MCQ', 'ShortAnswer'),
    IN questionOptions TEXT,
    IN correctAnswer TEXT
)
BEGIN
    INSERT INTO Question (exam_id, content, type, options, correct_answer)
    VALUES (examId, questionContent, questionType, questionOptions, correctAnswer);
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

CREATE PROCEDURE DeleteQuestion(
    IN questionId INT
)
BEGIN
    DELETE FROM Question WHERE question_id = questionId;
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

DELIMITER //

CREATE PROCEDURE CalculateExamScore(
    IN examId INT,
    IN studentId INT
)
BEGIN
    DECLARE totalQuestions INT;
    DECLARE correctAnswers INT;
    
    SELECT COUNT(*) INTO totalQuestions
    FROM Question
    WHERE exam_id = examId;
    
    SELECT COUNT(*) INTO correctAnswers
    FROM Answer a
    JOIN Question q ON a.question_id = q.question_id
    WHERE q.exam_id = examId 
    AND a.is_correct = TRUE
    AND a.submission_id IN (
        SELECT submission_id 
        FROM Submission 
        WHERE student_id = studentId 
        AND exam_id = examId
    );
    
    UPDATE Submission
    SET total_score = (correctAnswers * 100.0) / totalQuestions
    WHERE student_id = studentId 
    AND exam_id = examId;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE GetExamStatistics(
    IN examId INT
)
BEGIN
    SELECT 
        AVG(s.total_score) as average_score,
        MAX(s.total_score) as highest_score,
        MIN(s.total_score) as lowest_score,
        COUNT(*) as total_submissions
    FROM Submission s
    WHERE s.exam_id = examId;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE GetQuestionStatistics(
    IN questionId INT
)
BEGIN
    SELECT 
        COUNT(*) as total_attempts,
        SUM(CASE WHEN is_correct = TRUE THEN 1 ELSE 0 END) as correct_answers,
        (SUM(CASE WHEN is_correct = TRUE THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) as success_rate
    FROM Answer
    WHERE question_id = questionId;
END //

DELIMITER ;


