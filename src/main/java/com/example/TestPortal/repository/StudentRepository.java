package com.example.TestPortal.repository;

import com.example.TestPortal.model.Course;
import com.example.TestPortal.model.Student;
import com.example.TestPortal.model.Submission;
import com.example.TestPortal.model.Exam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import static com.example.TestPortal.util.TimeZoneUtil.IST;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;

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

    public List<Course> getStudentCourses(int studentId) {
        String sql = """
            SELECT c.* FROM Course c
            JOIN StudentCourse sc ON c.course_id = sc.course_id
            WHERE sc.student_id = ?
        """;
        return jdbcTemplate.query(sql, courseRowMapper(), studentId);
    }

    public List<Submission> getStudentSubmissions(int studentId) {
        String sql = """
            SELECT s.submission_id, e.title as exam_title, s.total_score, s.submission_date 
            FROM Submission s
            JOIN Exam e ON s.exam_id = e.exam_id
            WHERE s.student_id = ?
        """;
        return jdbcTemplate.query(sql, submissionRowMapper(), studentId);
    }

    public List<Exam> getAvailableExams(int courseId) {
        String sql = """
            SELECT DISTINCT e.* FROM Exam e
            JOIN Course c ON e.course_id = c.course_id
            WHERE e.course_id = ?
            AND e.start_time <= CURRENT_TIMESTAMP 
            AND e.end_time >= CURRENT_TIMESTAMP
            ORDER BY e.start_time ASC
        """;
        try {
            return jdbcTemplate.query(sql, examRowMapper(), courseId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
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

    private RowMapper<Submission> submissionRowMapper() {
        return (rs, _) -> {
            Submission submission = new Submission();
            submission.setSubmissionId(rs.getInt("submission_id"));
            submission.setExamTitle(rs.getString("exam_title"));
            submission.setTotalScore(rs.getDouble("total_score"));
            submission.setSubmissionDate(rs.getTimestamp("submission_date"));
            return submission;
        };
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
