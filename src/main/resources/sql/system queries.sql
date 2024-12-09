-- Shishtum queries
USE TestPortal;
DELIMITER //

CREATE PROCEDURE CalculateTotalScoreForSubmission(
    IN submission_id INT
)
BEGIN
    DECLARE correct_answers INT;

    -- Calculate the number of correct answers for this submission
    SELECT COUNT(*)
    INTO correct_answers
    FROM Answer a
    JOIN Submission s ON a.submission_id = s.submission_id
    WHERE s.submission_id = submission_id AND a.is_correct = TRUE;

    -- Update the total score for the submission (assuming each correct answer adds 1 point)
    UPDATE Submission
    SET total_score = correct_answers
    WHERE Submission.submission_id = submission_id;
END //

DELIMITER ;

-- UPDATE Submission s
-- JOIN (
--    SELECT submission_id, COUNT(*) AS correct_answers
--      FROM Answer
--      WHERE is_correct = TRUE
--      GROUP BY submission_id
--  ) AS correct_counts ON s.submission_id = correct_counts.submission_id
--  SET s.total_score = 6
--  WHERE s.submission_id = 2;

-- CALL CalculateTotalScoreForSubmission(2);
-- CALL GetStudentSubmissions(2);