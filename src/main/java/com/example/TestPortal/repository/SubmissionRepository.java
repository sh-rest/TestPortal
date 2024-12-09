package com.example.TestPortal.repository;

import com.example.TestPortal.model.Submission;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubmissionRepository {
    private final JdbcTemplate jdbcTemplate;

    public SubmissionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    //To Student
    public void createSubmission(Submission submission) {
        String sql = "INSERT INTO Submission (student_id, exam_id, total_score, submission_date) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, 
            submission.getStudentId(), 
            submission.getExamId(), 
            submission.getTotalScore(), 
            submission.getSubmissionDate()
        );
    }
    // To Teacher and Student
    public Submission getSubmissionById(int submissionId) {
        String sql = "SELECT * FROM Submission WHERE submission_id = ?";
        return jdbcTemplate.queryForObject(sql, submissionRowMapper(), submissionId);
    }
    // To Student and Teacher
    public List<Submission> getStudentSubmissions(int studentId) {
        String sql = "SELECT * FROM Submission WHERE student_id = ?";
        return jdbcTemplate.query(sql, submissionRowMapper(), studentId);
    }
    //To Teacher
    public List<Submission> getExamSubmissions(int examId) {
        String sql = "SELECT * FROM Submission WHERE exam_id = ?";
        return jdbcTemplate.query(sql, submissionRowMapper(), examId);
    }
    //Teacher
    public void updateSubmissionScore(int submissionId, double totalScore) {
        String sql = "UPDATE Submission SET total_score = ? WHERE submission_id = ?";
        jdbcTemplate.update(sql, totalScore, submissionId);
    }
    
    private RowMapper<Submission> submissionRowMapper() {
        return (rs, _) -> {
            Submission submission = new Submission();
            submission.setSubmissionId(rs.getInt("submission_id"));
            submission.setStudentId(rs.getInt("student_id"));
            submission.setExamId(rs.getInt("exam_id"));
            submission.setTotalScore(rs.getDouble("total_score"));
            submission.setSubmissionDate(rs.getTimestamp("submission_date"));
            return submission;
        };
    }
} 