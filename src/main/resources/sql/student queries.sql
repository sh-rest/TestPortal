-- student queries
USE TestPortal;

DELIMITER //

CREATE PROCEDURE GetAvailableCourses()
BEGIN
    SELECT c.course_id, c.course_name, c.description, t.name AS teacher_name
    FROM Course c
    JOIN Teacher t ON c.teacher_id = t.teacher_id;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE GetExamsForEnrolledCourses(
    IN student_id INT
)
BEGIN
    SELECT e.exam_id, e.title AS exam_title, e.description AS exam_description, e.date AS exam_date, c.course_name
    FROM Exam e
    JOIN Course c ON e.course_id = c.course_id
    JOIN StudentCourse sc ON sc.course_id = c.course_id
    WHERE sc.student_id = student_id;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE GetStudentCourses(IN student_id INT)
BEGIN
    SELECT c.course_id, c.course_name, c.description, t.name AS teacher_name
    FROM Course c
    JOIN Teacher t ON c.teacher_id = t.teacher_id
    JOIN StudentCourse sc ON sc.course_id = c.course_id
    WHERE sc.student_id = student_id;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE GetStudentSubmissions(IN student_id INT)
BEGIN
    SELECT
        sub.submission_id,
        e.title AS exam_title,
        sub.total_score,
        sub.submission_date
    FROM Submission sub
    JOIN Exam e ON sub.exam_id = e.exam_id
    WHERE sub.student_id = student_id;
END //

DELIMITER ;

CALL GetStudentSubmissions(2);