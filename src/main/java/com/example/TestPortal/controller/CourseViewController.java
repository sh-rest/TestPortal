package com.example.TestPortal.controller;

import com.example.TestPortal.model.Course;
import com.example.TestPortal.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/view/courses")
public class CourseViewController {
    private final AdminService adminService;

    public CourseViewController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable int id) {
        Course course = adminService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = adminService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
} 