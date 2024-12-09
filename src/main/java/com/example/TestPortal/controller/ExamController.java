package com.example.TestPortal.controller;

import com.example.TestPortal.model.Exam;
import com.example.TestPortal.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<Void> createExam(@RequestBody Exam exam) {
        examService.createExam(exam);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{examId}")
    public ResponseEntity<Exam> getExam(@PathVariable int examId) {
        return ResponseEntity.ok(examService.getExamById(examId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Exam>> getExamsByCourse(@PathVariable int courseId) {
        return ResponseEntity.ok(examService.getExamsByCourse(courseId));
    }
} 