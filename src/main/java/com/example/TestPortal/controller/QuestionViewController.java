package com.example.TestPortal.controller;

import com.example.TestPortal.model.QuestionStatistics;
import com.example.TestPortal.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/view/questions")
public class QuestionViewController {
    private final TeacherService teacherService;

    public QuestionViewController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/{questionId}/statistics")
    public ResponseEntity<QuestionStatistics> getQuestionStatistics(@PathVariable int questionId) {
        return ResponseEntity.ok(teacherService.getQuestionStatistics(questionId));
    }
} 