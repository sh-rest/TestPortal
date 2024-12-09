package com.example.TestPortal.controller;

import com.example.TestPortal.model.Exam;
import com.example.TestPortal.model.Question;
import com.example.TestPortal.model.ExamStatistics;
import com.example.TestPortal.service.TeacherService;
import com.example.TestPortal.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/view/exams")
public class ExamViewController {
    private final TeacherService teacherService;
    private final ExamService examService;

    public ExamViewController(TeacherService teacherService, ExamService examService) {
        this.teacherService = teacherService;
        this.examService = examService;
    }

    @GetMapping("/{examId}")
    public ResponseEntity<Exam> getExamById(@PathVariable int examId) {
        return ResponseEntity.ok(examService.getExamById(examId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Exam>> getExamsByCourse(@PathVariable int courseId) {
        return ResponseEntity.ok(examService.getExamsByCourse(courseId));
    }

    @GetMapping("/{examId}/questions")
    public ResponseEntity<List<Question>> getExamQuestions(@PathVariable int examId) {
        return ResponseEntity.ok(teacherService.getExamQuestions(examId));
    }

    @GetMapping("/{examId}/statistics")
    public ResponseEntity<ExamStatistics> getExamStatistics(@PathVariable int examId) {
        return ResponseEntity.ok(teacherService.getExamStatistics(examId));
    }
}