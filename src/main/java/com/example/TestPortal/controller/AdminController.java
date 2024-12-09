package com.example.TestPortal.controller;

import com.example.TestPortal.model.Student;
import com.example.TestPortal.model.Teacher;
import com.example.TestPortal.model.Course;
import com.example.TestPortal.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        adminService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable int id, @RequestBody Student student) {
        adminService.updateStudent(id, student);
        return ResponseEntity.ok().build();
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
} 