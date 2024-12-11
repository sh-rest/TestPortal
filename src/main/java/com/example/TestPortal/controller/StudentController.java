package com.example.TestPortal.controller;

import com.example.TestPortal.model.Course;
import com.example.TestPortal.model.Student;
import com.example.TestPortal.model.Submission;
import com.example.TestPortal.model.Exam;
import com.example.TestPortal.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable int id) {
        return studentService.getStudentById(id);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{studentId}/courses")
    public List<Course> getStudentCourses(@PathVariable int studentId) {
        return studentService.getStudentCourses(studentId);
    }

    @GetMapping("/{studentId}/submissions")
    public List<Submission> getStudentSubmissions(@PathVariable int studentId) {
        return studentService.getStudentSubmissions(studentId);
    }

    @GetMapping("/courses/{courseId}/available-tests")
    public ResponseEntity<List<Exam>> getAvailableTests(@PathVariable int courseId) {
        try {
            List<Exam> availableExams = studentService.getAvailableExams(courseId);
            return ResponseEntity.ok(availableExams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 