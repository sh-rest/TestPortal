package com.example.TestPortal.repository;

import com.example.TestPortal.model.Exam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ExamRepository {
    private final JdbcTemplate jdbcTemplate;

    public ExamRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createExam(Exam exam) {
        String sql = "INSERT INTO Exam (title, description, date, course_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, exam.getTitle(), exam.getDescription(), exam.getDate(), exam.getCourseId());
    }

    public Exam getExamById(int examId) {
        String sql = "SELECT * FROM Exam WHERE exam_id = ?";
        return jdbcTemplate.queryForObject(sql, examRowMapper(), examId);
    }

    public List<Exam> getExamsByCourse(int courseId) {
        String sql = "SELECT * FROM Exam WHERE course_id = ?";
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
            return exam;
        };
    }
}