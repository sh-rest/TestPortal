package com.example.TestPortal.repository;

import com.example.TestPortal.model.Exam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import static com.example.TestPortal.util.TimeZoneUtil.IST;

@Repository
public class ExamRepository {
    private final JdbcTemplate jdbcTemplate;

    public ExamRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createExam(Exam exam) {
        String sql = """
            INSERT INTO Exam (title, description, date, course_id, 
            duration, start_time, end_time) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql, 
            exam.getTitle(), 
            exam.getDescription(), 
            new Timestamp(exam.getDate().getTime()),
            exam.getCourseId(),
            exam.getDuration(),
            new Timestamp(exam.getStartTime().getTime()),
            new Timestamp(exam.getEndTime().getTime())
        );
    }

    public Exam getExamById(int examId) {
        String sql = "SELECT * FROM Exam WHERE exam_id = ?";
        return jdbcTemplate.queryForObject(sql, examRowMapper(), examId);
    }

    public List<Exam> getExamsByCourse(int courseId) {
        String sql = "SELECT * FROM Exam WHERE course_id = ?";
        return jdbcTemplate.query(sql, examRowMapper(), courseId);
    }

    public List<Exam> getAvailableExams(int courseId) {
        String sql = """
            SELECT * FROM Exam 
            WHERE course_id = ? 
            AND start_time <= CURRENT_TIMESTAMP 
            AND end_time >= CURRENT_TIMESTAMP
        """;
        return jdbcTemplate.query(sql, examRowMapper(), courseId);
    }

    private RowMapper<Exam> examRowMapper() {
        return (rs, _) -> {
            Exam exam = new Exam();
            exam.setExamId(rs.getInt("exam_id"));
            exam.setTitle(rs.getString("title"));
            exam.setDescription(rs.getString("description"));
            exam.setDate(rs.getTimestamp("date"));
            exam.setCourseId(rs.getInt("course_id"));
            exam.setDuration(rs.getInt("duration"));
            exam.setStartTime(rs.getTimestamp("start_time"));
            exam.setEndTime(rs.getTimestamp("end_time"));
            return exam;
        };
    }
}