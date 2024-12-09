package com.example.TestPortal.controller;

import com.example.TestPortal.model.Student;
import com.example.TestPortal.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/view/students")
public class StudentViewController {
    private final AdminService adminService;

    public StudentViewController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        Student student = adminService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = adminService.getAllStudents();
        return ResponseEntity.ok(students);
    }
} 