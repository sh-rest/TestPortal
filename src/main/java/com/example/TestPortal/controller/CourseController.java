package com.example.TestPortal.controller;

import com.example.TestPortal.model.Course;
import com.example.TestPortal.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public void addCourse(@RequestBody Course course) {
        courseService.addCourse(course);
    }

    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable int courseId) {
        courseService.deleteCourse(courseId);
    }

    @PutMapping("/{courseId}")
    public void updateCourse(@PathVariable int courseId, @RequestBody Course course) {
        course.setCourseId(courseId);
        courseService.updateCourse(course);
    }

    @GetMapping("/{courseId}")
    public Course getCourseById(@PathVariable int courseId) {
        return courseService.getCourseById(courseId);
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }
} 