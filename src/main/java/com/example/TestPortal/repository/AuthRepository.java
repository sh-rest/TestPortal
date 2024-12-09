package com.example.TestPortal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthRepository {
    private final JdbcTemplate jdbcTemplate;

    public AuthRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer authenticateUser(String email, String password, String userType) {
        String sql = switch (userType.toUpperCase()) {
            case "ADMIN" -> "SELECT admin_id FROM Admin WHERE email = ? AND password = ?";
            case "TEACHER" -> "SELECT teacher_id FROM Teacher WHERE email = ? AND password = ?";
            case "STUDENT" -> "SELECT student_id FROM Student WHERE email = ? AND password = ?";
            default -> throw new IllegalArgumentException("Invalid user type");
        };

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, email, password);
        } catch (Exception e) {
            return null;
        }
    }
} 