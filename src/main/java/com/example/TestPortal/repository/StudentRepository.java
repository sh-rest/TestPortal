package com.example.TestPortal.repository;

import com.example.TestPortal.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRepository {
    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Student getStudentById(int student_id) {
        String sql = "SELECT * FROM Student WHERE student_id = ?";
        return jdbcTemplate.queryForObject(sql, studentRowMapper(), student_id);
    }

    public List<Student> getAllStudents() {
        String sql = "SELECT * FROM Student";
        return jdbcTemplate.query(sql, studentRowMapper());
    }

    private RowMapper<Student> studentRowMapper() {
        return (rs, _) -> {
            Student student = new Student();
            student.setStudentId(rs.getInt("student_id"));
            student.setName(rs.getString("name"));
            student.setEmail(rs.getString("email"));
            // Note: We typically don't want to expose password data
            return student;
        };
    }
}
