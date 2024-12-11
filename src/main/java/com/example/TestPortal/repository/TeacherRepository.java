package com.example.TestPortal.repository;

import com.example.TestPortal.model.Exam;
import com.example.TestPortal.model.Question;
import com.example.TestPortal.model.Student;
import com.example.TestPortal.model.Course;
import com.example.TestPortal.model.ExamStatistics;
import com.example.TestPortal.model.QuestionStatistics;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.time.LocalDateTime;
import java.sql.Timestamp;

@Repository
public class TeacherRepository {
    private final JdbcTemplate jdbcTemplate;

    public TeacherRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createExam(String title, String description, LocalDateTime date, 
        int courseId, int duration, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = """
            INSERT INTO Exam (title, description, date, course_id, duration, start_time, end_time) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setTimestamp(3, Timestamp.valueOf(date));
            ps.setInt(4, courseId);
            ps.setInt(5, duration);
            ps.setTimestamp(6, Timestamp.valueOf(startTime));
            ps.setTimestamp(7, Timestamp.valueOf(endTime));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void addQuestion(Question question) {
        String sql = "INSERT INTO Question (exam_id, content, type, options, correct_answer) " +
                    "VALUES (?, ?, ?, ?, ?)";
                
        jdbcTemplate.update(sql,
            question.getExamId(),
            question.getContent(),
            question.getType(),
            question.getOptions(),
            question.getCorrectAnswer()
        );
    }

    public void deleteQuestion(int questionId) {
        String sql = "DELETE FROM Question WHERE question_id = ?";
        if(questionId > 0){
            jdbcTemplate.update(sql, questionId);
        }

        // jdbcTemplate.update("CALL DeleteQuestion(?)", questionId);
    }

    public List<Exam> getTeacherExams(int teacherId) {
        String sql = """
            SELECT e.* 
            FROM Exam e 
            JOIN Course c ON e.course_id = c.course_id 
            WHERE c.teacher_id = ?
            ORDER BY e.start_time DESC
        """;
        return jdbcTemplate.query(sql, examRowMapper(), teacherId);
    }

    public void updateExam(int examId, String title, String description, 
        LocalDateTime date, int duration, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = """
            UPDATE Exam SET title = ?, description = ?, date = ?, 
            duration = ?, start_time = ?, end_time = ? 
            WHERE exam_id = ?
        """;
        jdbcTemplate.update(sql, title, description, Timestamp.valueOf(date), 
            duration, Timestamp.valueOf(startTime), Timestamp.valueOf(endTime), examId);
    }

    public List<Question> getExamQuestions(int examId) {
        String sql = "SELECT question_id, exam_id, content, type, options, correct_answer " +
                     "FROM Question " +
                     "WHERE exam_id = ?";
        
        return jdbcTemplate.query(sql, questionRowMapper(), examId);
    }

    public List<Student> getEnrolledStudents(int teacherId, int courseId) {
        String sql = "SELECT s.student_id, s.name, s.email " +
                     "FROM Student s " +
                     "JOIN StudentCourse sc ON s.student_id = sc.student_id " +
                     "WHERE sc.course_id = ? " +
                     "AND EXISTS (SELECT 1 FROM Course c " +
                     "           WHERE c.course_id = ? " +
                     "           AND c.teacher_id = ?)";
        return jdbcTemplate.query(sql, studentRowMapper(), courseId, courseId, teacherId);
    }

    public List<Course> getTeacherCourses(int teacherId) {
        String sql = "SELECT course_id, course_name, description, teacher_id " +
                     "FROM Course " +
                     "WHERE teacher_id = ?";
        return jdbcTemplate.query(sql, courseRowMapper(), teacherId);
    }

    public void updateQuestion(Question question) {
        jdbcTemplate.update("CALL UpdateQuestionInExam(?, ?, ?, ?, ?)",
            question.getQuestionId(),
            question.getContent(),
            question.getType(),
            question.getOptions(),
            question.getCorrectAnswer()
        );
    }

    public ExamStatistics getExamStatistics(int examId) {
        String sql = "SELECT " +
                     "AVG(s.total_score) as average_score, " +
                     "MAX(s.total_score) as highest_score, " +
                     "MIN(s.total_score) as lowest_score, " +
                     "COUNT(*) as total_submissions " +
                     "FROM Submission s " +
                     "WHERE s.exam_id = ?";
        
        return jdbcTemplate.queryForObject(sql, (rs, _) -> {
            ExamStatistics stats = new ExamStatistics();
            stats.setAverageScore(rs.getDouble("average_score"));
            stats.setHighestScore(rs.getDouble("highest_score"));
            stats.setLowestScore(rs.getDouble("lowest_score"));
            stats.setTotalSubmissions(rs.getInt("total_submissions"));
            return stats;
        }, examId);
    }

    public QuestionStatistics getQuestionStatistics(int questionId) {
        String sql = "SELECT " +
                     "COUNT(*) as total_attempts, " +
                     "SUM(CASE WHEN is_correct = TRUE THEN 1 ELSE 0 END) as correct_answers, " +
                     "(SUM(CASE WHEN is_correct = TRUE THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) as success_rate " +
                     "FROM Answer " +
                     "WHERE question_id = ?";
        
        return jdbcTemplate.queryForObject(sql, (rs, _) -> {
            QuestionStatistics stats = new QuestionStatistics();
            stats.setTotalAttempts(rs.getInt("total_attempts"));
            stats.setCorrectAnswers(rs.getInt("correct_answers"));
            stats.setSuccessRate(rs.getDouble("success_rate"));
            return stats;
        }, questionId);
    }

    private org.springframework.jdbc.core.RowMapper<Exam> examRowMapper() {
        return (rs, _) -> {
            Exam exam = new Exam();
            exam.setExamId(rs.getInt("exam_id"));
            exam.setTitle(rs.getString("title"));
            exam.setDescription(rs.getString("description"));
            exam.setDate(rs.getTimestamp("date").toLocalDateTime());
            exam.setCourseId(rs.getInt("course_id"));
            exam.setDuration(rs.getInt("duration"));
            exam.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
            exam.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
            return exam;
        };
    }

    private org.springframework.jdbc.core.RowMapper<Question> questionRowMapper() {
        return (rs, _) -> {
            Question question = new Question();
            question.setQuestionId(rs.getInt("question_id"));
            question.setContent(rs.getString("content"));
            question.setType(rs.getString("type"));
            question.setOptions(rs.getString("options"));
            question.setCorrectAnswer(rs.getString("correct_answer"));
            return question;
        };
    }

    private org.springframework.jdbc.core.RowMapper<Student> studentRowMapper() {
        return (rs, _) -> {
            Student student = new Student();
            student.setStudentId(rs.getInt("student_id"));
            student.setName(rs.getString("name"));
            student.setEmail(rs.getString("email"));
            return student;
        };
    }

    private org.springframework.jdbc.core.RowMapper<Course> courseRowMapper() {
        return (rs, _) -> {
            Course course = new Course();
            course.setCourseId(rs.getInt("course_id"));
            course.setCourseName(rs.getString("course_name"));
            course.setDescription(rs.getString("description"));
            course.setTeacherId(rs.getInt("teacher_id"));
            return course;
        };
    }
} 