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

@Repository
public class TeacherRepository {
    private final JdbcTemplate jdbcTemplate;

    public TeacherRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createExam(String title, String description, java.util.Date date, int courseId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "CALL CreateExam(?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setTimestamp(3, new java.sql.Timestamp(date.getTime()));
            ps.setInt(4, courseId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void addQuestion(Question question) {
        jdbcTemplate.update("CALL AddQuestion(?, ?, ?, ?, ?)",
            question.getExamId(),
            question.getContent(),
            question.getType(),
            question.getOptions(),
            question.getCorrectAnswer()
        );
    }

    public void deleteQuestion(int questionId) {
        jdbcTemplate.update("CALL DeleteQuestion(?)", questionId);
    }

    public List<Exam> getTeacherExams(int teacherId) {
        String sql = "SELECT e.* FROM Exam e " +
                    "JOIN Course c ON e.course_id = c.course_id " +
                    "WHERE c.teacher_id = ?";
        return jdbcTemplate.query(sql, examRowMapper(), teacherId);
    }

    public void updateExam(int examId, String title, String description, java.util.Date date) {
        jdbcTemplate.update("CALL UpdateExam(?, ?, ?, ?)", 
            examId, title, description, new java.sql.Timestamp(date.getTime()));
    }

    public List<Question> getExamQuestions(int examId) {
        String sql = "CALL ViewQuestionsInExam(?)";
        return jdbcTemplate.query(sql, questionRowMapper(), examId);
    }

    public List<Student> getEnrolledStudents(int teacherId, int courseId) {
        String sql = "CALL GetEnrolledStudents(?, ?)";
        return jdbcTemplate.query(sql, studentRowMapper(), teacherId, courseId);
    }

    public List<Course> getTeacherCourses(int teacherId) {
        String sql = "CALL GetCoursesByTeacher(?)";
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
        String sql = "CALL GetExamStatistics(?)";
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
        String sql = "CALL GetQuestionStatistics(?)";
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
            exam.setDate(rs.getTimestamp("date"));
            exam.setCourseId(rs.getInt("course_id"));
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