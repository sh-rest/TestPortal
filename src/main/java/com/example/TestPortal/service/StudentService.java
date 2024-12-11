package com.example.TestPortal.service;

import com.example.TestPortal.model.Course;
import com.example.TestPortal.model.Student;
import com.example.TestPortal.model.Submission;
import com.example.TestPortal.model.Exam;
import com.example.TestPortal.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student getStudentById(int id) {
        return studentRepository.getStudentById(id);
    }

    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    public List<Course> getStudentCourses(int studentId) {
        return studentRepository.getStudentCourses(studentId);
    }

    public List<Submission> getStudentSubmissions(int studentId) {
        return studentRepository.getStudentSubmissions(studentId);
    }

    public List<Exam> getAvailableExams(int courseId) {
        return studentRepository.getAvailableExams(courseId);
    }
} 