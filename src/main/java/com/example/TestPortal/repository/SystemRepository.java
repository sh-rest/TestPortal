package com.example.TestPortal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SystemRepository {
    private final JdbcTemplate jdbcTemplate;

    public SystemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    // AUtomatically call after a student submits the exam
    public void calculateTotalScoreForSubmission(int submissionId) {
        String sql = "CALL CalculateTotalScoreForSubmission(?)";
        jdbcTemplate.update(sql, submissionId);
    }

    // Add other system-related methods as needed
} 