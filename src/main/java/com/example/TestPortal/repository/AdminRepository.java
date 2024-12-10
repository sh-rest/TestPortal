package com.example.TestPortal.repository;

import com.example.TestPortal.model.Admin;
import com.example.TestPortal.model.Teacher;
import com.example.TestPortal.model.Student;
import com.example.TestPortal.model.Course;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class AdminRepository {
    private final JdbcTemplate jdbcTemplate;

    public AdminRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Student Management
    public void addStudent(String name, String email, String password) {
        jdbcTemplate.update("CALL AddStudent(?, ?, ?)", name, email, password);
    }

    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM Student WHERE student_id = ?";
        return jdbcTemplate.queryForObject(sql, studentRowMapper(), studentId);
    }

    public List<Student> getAllStudents() {
        String sql = "SELECT * FROM Student";
        return jdbcTemplate.query(sql, studentRowMapper());
    }

    public void deleteStudent(int studentId) {
        jdbcTemplate.update("CALL DeleteStudent(?)", studentId);
    }

    public void updateStudent(int studentId, String name, String email, String password) {
        jdbcTemplate.update("CALL UpdateStudent(?, ?, ?, ?)", studentId, name, email, password);
    }

    // Teacher Management
    public void addTeacher(String name, String email, String password) {
        jdbcTemplate.update("CALL AddTeacher(?, ?, ?)", name, email, password);
    }

    public Teacher getTeacherById(int teacherId) {
        String sql = "SELECT * FROM Teacher WHERE teacher_id = ?";
        return jdbcTemplate.queryForObject(sql, teacherRowMapper(), teacherId);
    }

    public List<Teacher> getAllTeachers() {
        String sql = "SELECT * FROM Teacher";
        return jdbcTemplate.query(sql, teacherRowMapper());
    }

    public void deleteTeacher(int teacherId) {
        jdbcTemplate.update("CALL DeleteTeacher(?)", teacherId);
    }

    public void updateTeacher(int teacherId, String name, String email, String password) {
        jdbcTemplate.update("CALL UpdateTeacher(?, ?, ?, ?)", teacherId, name, email, password);
    }

    // Course Management
    public void addCourse(String courseName, String description, int teacherId) {
        jdbcTemplate.update("CALL AddCourse(?, ?, ?)", courseName, description, teacherId);
    }

    public Course getCourseById(int courseId) {
        String sql = "SELECT * FROM Course WHERE course_id = ?";
        return jdbcTemplate.queryForObject(sql, courseRowMapper(), courseId);
    }

    public List<Course> getAllCourses() {
        String sql = "SELECT * FROM Course";
        return jdbcTemplate.query(sql, courseRowMapper());
    }

    public void assignTeacherToCourse(int courseId, int teacherId) {
        jdbcTemplate.update("CALL AssignTeacherToCourse(?, ?)", courseId, teacherId);
    }

    public void deleteCourse(int courseId) {
        jdbcTemplate.update("CALL DeleteCourse(?)", courseId);
    }

    public void updateCourse(int courseId, String courseName, String description, int teacherId) {
        jdbcTemplate.update("CALL UpdateCourse(?, ?, ?, ?)", 
            courseId, courseName, description, teacherId);
    }

    // Enrollment Management
    public void enrollStudent(int studentId, int courseId) {
        jdbcTemplate.update("CALL EnrollStudent(?, ?)", studentId, courseId);
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        jdbcTemplate.update("CALL RemoveStudentFromCourse(?, ?)", studentId, courseId);
    }

    // Admin Management
    public Admin getAdminById(int adminId) {
        String sql = "SELECT * FROM Admin WHERE admin_id = ?";
        return jdbcTemplate.queryForObject(sql, adminRowMapper());
    }

    public List<Map<String, Object>> getAllEnrollments() {
        String sql = """
            SELECT s.student_id, s.name as student_name, 
                   c.course_id, c.course_name
            FROM Student s
            JOIN StudentCourse sc ON s.student_id = sc.student_id
            JOIN Course c ON c.course_id = sc.course_id
            ORDER BY s.student_id, c.course_id
        """;
        
        return jdbcTemplate.queryForList(sql);
    }

    // Row Mappers
    private RowMapper<Student> studentRowMapper() {
        return (rs, _) -> {
            Student student = new Student();
            student.setStudentId(rs.getInt("student_id"));
            student.setName(rs.getString("name"));
            student.setEmail(rs.getString("email"));
            student.setPassword(rs.getString("password"));
            return student;
        };
    }

    private RowMapper<Teacher> teacherRowMapper() {
        return (rs, _) -> {
            Teacher teacher = new Teacher();
            teacher.setTeacherId(rs.getInt("teacher_id"));
            teacher.setName(rs.getString("name"));
            teacher.setEmail(rs.getString("email"));
            teacher.setPassword(rs.getString("password"));
            return teacher;
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

    private RowMapper<Admin> adminRowMapper() {
        return (rs, _) -> {
            Admin admin = new Admin();
            admin.setAdminId(rs.getInt("admin_id"));
            admin.setName(rs.getString("name"));
            admin.setEmail(rs.getString("email"));
            admin.setPassword(rs.getString("password"));
            return admin;
        };
    }
} 