package com.example.TestPortal.repository;

import com.example.TestPortal.model.Course;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseRepository {
    private final JdbcTemplate jdbcTemplate;

    public CourseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addCourse(Course course) {
        String sql = "INSERT INTO Course (course_name, description, teacher_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, course.getCourseName(), course.getDescription(), course.getTeacherId());
    }

    public void deleteCourse(int courseId) {
        String sql = "DELETE FROM Course WHERE course_id = ?";
        jdbcTemplate.update(sql, courseId);
    }

    public void updateCourse(Course course) {
        String sql = "UPDATE Course SET course_name = ?, description = ?, teacher_id = ? WHERE course_id = ?";
        jdbcTemplate.update(sql, course.getCourseName(), course.getDescription(), course.getTeacherId(), course.getCourseId());
    }

    public Course getCourseById(int courseId) {
        String sql = "SELECT * FROM Course WHERE course_id = ?";
        return jdbcTemplate.queryForObject(sql, courseRowMapper(), courseId);
    }

    public List<Course> getAllCourses() {
        String sql = "SELECT * FROM Course";
        return jdbcTemplate.query(sql, courseRowMapper());
    }

    private RowMapper<Course> courseRowMapper() {
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