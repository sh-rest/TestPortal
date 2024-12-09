package com.example.TestPortal.service;

import com.example.TestPortal.model.Course;
import com.example.TestPortal.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void addCourse(Course course) {
        courseRepository.addCourse(course);
    }

    public void deleteCourse(int courseId) {
        courseRepository.deleteCourse(courseId);
    }

    public void updateCourse(Course course) {
        courseRepository.updateCourse(course);
    }

    public Course getCourseById(int courseId) {
        return courseRepository.getCourseById(courseId);
    }

    public List<Course> getAllCourses() {
        return courseRepository.getAllCourses();
    }
} 