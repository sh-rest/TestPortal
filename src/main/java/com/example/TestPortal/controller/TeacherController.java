package com.example.TestPortal.controller;

import com.example.TestPortal.model.Course;
import com.example.TestPortal.model.Exam;
import com.example.TestPortal.model.ExamStatistics;
import com.example.TestPortal.model.Question;
import com.example.TestPortal.model.QuestionStatistics;
import com.example.TestPortal.model.Student;
import com.example.TestPortal.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping("/{teacherId}/exams")
    public ResponseEntity<Integer> createExam(@RequestBody Exam exam) {
        int examId = teacherService.createExam(exam);
        return ResponseEntity.ok(examId);
    }

    @PostMapping("/exams/{examId}/questions")
    public ResponseEntity<Void> addQuestion(@PathVariable int examId, @RequestBody Question question) {
        question.setExamId(examId);
        teacherService.addQuestion(question);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable int questionId) {
        teacherService.deleteQuestion(questionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{teacherId}/exams")
    public ResponseEntity<List<Exam>> getTeacherExams(@PathVariable int teacherId) {
        return ResponseEntity.ok(teacherService.getTeacherExams(teacherId));
    }

    @PutMapping("/exams/{examId}")
    public ResponseEntity<Void> updateExam(@PathVariable int examId, @RequestBody Exam exam) {
        teacherService.updateExam(examId, exam);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exams/{examId}/questions")
    public ResponseEntity<List<Question>> getExamQuestions(@PathVariable int examId) {
        return ResponseEntity.ok(teacherService.getExamQuestions(examId));
    }

    @GetMapping("/{teacherId}/courses/{courseId}/students")
    public ResponseEntity<List<Student>> getEnrolledStudents(
            @PathVariable int teacherId, 
            @PathVariable int courseId) {
        return ResponseEntity.ok(teacherService.getEnrolledStudents(teacherId, courseId));
    }

    @GetMapping("/{teacherId}/courses")
    public ResponseEntity<List<Course>> getTeacherCourses(@PathVariable int teacherId) {
        return ResponseEntity.ok(teacherService.getTeacherCourses(teacherId));
    }

    @PutMapping("/questions/{questionId}")
    public ResponseEntity<Void> updateQuestion(
            @PathVariable int questionId,
            @RequestBody Question question) {
        question.setQuestionId(questionId);
        teacherService.updateQuestion(question);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exams/{examId}/statistics")
    public ResponseEntity<ExamStatistics> getExamStatistics(@PathVariable int examId) {
        return ResponseEntity.ok(teacherService.getExamStatistics(examId));
    }

    @GetMapping("/questions/{questionId}/statistics")
    public ResponseEntity<QuestionStatistics> getQuestionStatistics(@PathVariable int questionId) {
        return ResponseEntity.ok(teacherService.getQuestionStatistics(questionId));
    }
}