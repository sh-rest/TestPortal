package com.example.TestPortal.controller;

import com.example.TestPortal.model.Student;
import com.example.TestPortal.model.Teacher;
import com.example.TestPortal.model.Course;
import com.example.TestPortal.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Student Management
    @PostMapping("/students")
    public ResponseEntity<Void> addStudent(@RequestBody Student student) {
        adminService.addStudent(student);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
        try {
            Student student = adminService.getStudentById(id);
            if (student == null) {
                return ResponseEntity.notFound().build();
            }
            adminService.deleteStudent(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable int id, @RequestBody Student student) {
        adminService.updateStudent(id, student);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable int id) {
        try {
            Student student = adminService.getStudentById(id);
            if (student == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Teacher Management
    @PostMapping("/teachers")
    public ResponseEntity<Void> addTeacher(@RequestBody Teacher teacher) {
        adminService.addTeacher(teacher);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable int id) {
        adminService.deleteTeacher(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/teachers/{id}")
    public ResponseEntity<Void> updateTeacher(@PathVariable int id, @RequestBody Teacher teacher) {
        adminService.updateTeacher(id, teacher);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teachers/{id}")
    public ResponseEntity<Teacher> getTeacher(@PathVariable int id) {
        try {
            Teacher teacher = adminService.getTeacherById(id);
            return ResponseEntity.ok(teacher);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Course Management
    @PostMapping("/courses")
    public ResponseEntity<Void> addCourse(@RequestBody Course course) {
        adminService.addCourse(course);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/courses/{courseId}/teachers/{teacherId}")
    public ResponseEntity<Void> assignTeacherToCourse(@PathVariable int courseId, @PathVariable int teacherId) {
        adminService.assignTeacherToCourse(courseId, teacherId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        adminService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<Void> updateCourse(@PathVariable int id, @RequestBody Course course) {
        adminService.updateCourse(id, course);
        return ResponseEntity.ok().build();
    }

    // Enrollment Management
    @PostMapping("/enrollments/students/{studentId}/courses/{courseId}")
    public ResponseEntity<Void> enrollStudent(@PathVariable int studentId, @PathVariable int courseId) {
        adminService.enrollStudent(studentId, courseId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/enrollments/students/{studentId}/courses/{courseId}")
    public ResponseEntity<Void> removeStudentFromCourse(@PathVariable int studentId, @PathVariable int courseId) {
        adminService.removeStudentFromCourse(studentId, courseId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/enrollments")
    public ResponseEntity<List<Map<String, Object>>> getAllEnrollments() {
        try {
            List<Map<String, Object>> enrollments = adminService.getAllEnrollments();
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/students/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        try {
            List<Student> students = adminService.getAllStudents();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/teachers/all")
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        try {
            List<Teacher> teachers = adminService.getAllTeachers();
            return ResponseEntity.ok(teachers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/courses/all")
    public ResponseEntity<List<Course>> getAllCourses() {
        try {
            List<Course> courses = adminService.getAllCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 