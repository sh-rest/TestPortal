package com.example.TestPortal.controller;

import com.example.TestPortal.model.Teacher;
import com.example.TestPortal.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/view/teachers")
public class TeacherViewController {
    private final AdminService adminService;

    public TeacherViewController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable int id) {
        Teacher teacher = adminService.getTeacherById(id);
        return ResponseEntity.ok(teacher);
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = adminService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }
} 