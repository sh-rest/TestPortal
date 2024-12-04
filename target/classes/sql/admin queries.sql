USE TestPortal;
DELIMITER //

CREATE PROCEDURE AddStudent(
    IN studentName VARCHAR(255),
    IN studentEmail VARCHAR(255),
    IN studentPassword VARCHAR(255)
)
BEGIN
    INSERT INTO Student (name, email, password)
    VALUES (studentName, studentEmail, studentPassword);
END //

DELIMITER ;
-- CALL AddStudent('Jane Doe', 'jane.doe@example.com', 'jane123');
-- CALL AddStudent('Faran Akhtar', 'faran@example.com', 'faran123');
DELIMITER //
--

CREATE PROCEDURE AddTeacher(
    IN teacherName VARCHAR(255),
    IN teacherEmail VARCHAR(255),
    IN teacherPassword VARCHAR(255)
)
BEGIN
    INSERT INTO Teacher (name, email, password)
    VALUES (teacherName, teacherEmail, teacherPassword);
END //

DELIMITER ;
-- CALL AddTeacher('Prof. Xavier', 'xavier@example.com', 'xavier123');


DELIMITER //

CREATE PROCEDURE DeleteStudent(
    IN studentID INT
)
BEGIN
    DELETE FROM Student
    WHERE student_id = studentID;
END //

DELIMITER ;
-- CALL DeleteStudent(1); -- Replace 1 with the student_id

DELIMITER //

CREATE PROCEDURE DeleteTeacher(
    IN teacherID INT
)
BEGIN
    DELETE FROM Teacher
    WHERE teacher_id = teacherID;
END //

DELIMITER ;
-- CALL DeleteTeacher(1); -- Replace 1 with the teacher_id

DELIMITER //

CREATE PROCEDURE UpdateStudent(
    IN studentID INT,
    IN newName VARCHAR(255),
    IN newEmail VARCHAR(255),
    IN newPassword VARCHAR(255)
)
BEGIN
    UPDATE Student
    SET name = newName, email = newEmail, password = newPassword
    WHERE student_id = studentID;
END //

DELIMITER ;
-- CALL UpdateStudent(1, 'John Doe', 'john.doe@example.com', 'newpass123');


DELIMITER //

CREATE PROCEDURE UpdateTeacher(
    IN teacherID INT,
    IN newName VARCHAR(255),
    IN newEmail VARCHAR(255),
    IN newPassword VARCHAR(255)
)
BEGIN
    UPDATE Teacher
    SET name = newName, email = newEmail, password = newPassword
    WHERE teacher_id = teacherID;
END //

DELIMITER ;
-- CALL UpdateTeacher(1, 'Dr. Strange', 'strange@example.com', 'strange123');

DELIMITER //

CREATE PROCEDURE AssignTeacherToCourse(
    IN courseID INT,
    IN teacherID INT
)
BEGIN
    UPDATE Course
    SET teacher_id = teacherID
    WHERE course_id = courseID;
END //

DELIMITER ;
-- CALL AssignTeacherToCourse(1, 2); -- Assign teacher with ID 2 to course with ID 1

DELIMITER //

CREATE PROCEDURE AddCourse(
    IN courseName VARCHAR(255),
    IN courseDescription TEXT,
    IN teacherID INT
)
BEGIN
    INSERT INTO Course (course_name, description, teacher_id)
    VALUES (courseName, courseDescription, teacherID);
END //

DELIMITER ;

-- CALL AddCourse('Biology 101', 'Introduction to Biology', 1);

DELIMITER //

CREATE PROCEDURE EnrollStudent(
    IN studentID INT,
    IN courseID INT
)
BEGIN
    INSERT INTO StudentCourse (student_id, course_id)
    VALUES (studentID, courseID);
END //

DELIMITER ;

-- CALL EnrollStudent(4,1);
-- CALL EnrollStudent(2, 1); -- Enroll student with ID 2 in course with ID 1

DELIMITER //

CREATE PROCEDURE RemoveStudentFromCourse(
    IN studentID INT,
    IN courseID INT
)
BEGIN
    DELETE FROM StudentCourse
    WHERE student_id = studentID AND course_id = courseID;
END //

DELIMITER ;

-- CALL RemoveStudentFromCourse(2, 1); -- Remove student with ID 2 from course with ID 1


