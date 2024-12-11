package com.example.TestPortal.controller;

import com.example.TestPortal.model.Course;
import com.example.TestPortal.model.Exam;
import com.example.TestPortal.model.ExamStatistics;
import com.example.TestPortal.model.Question;
import com.example.TestPortal.model.QuestionStatistics;
import com.example.TestPortal.model.Student;
import com.example.TestPortal.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);
    private final JdbcTemplate jdbcTemplate;

    public TeacherController(TeacherService teacherService, JdbcTemplate jdbcTemplate) {
        this.teacherService = teacherService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/{teacherId}/exams")
    public ResponseEntity<Integer> createExam(@PathVariable int teacherId, @RequestBody Exam exam) {
        try {
            // Log the incoming request
            logger.info("Creating exam for teacher {}: {}", teacherId, exam);
            
            // Verify the course belongs to the teacher
            String verifySql = "SELECT COUNT(*) FROM Course WHERE course_id = ? AND teacher_id = ?";
            int count = jdbcTemplate.queryForObject(verifySql, Integer.class, exam.getCourseId(), teacherId);
            
            if (count == 0) {
                logger.error("Course {} not found or not authorized for teacher {}", exam.getCourseId(), teacherId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            int examId = teacherService.createExam(exam);
            logger.info("Successfully created exam with ID: {}", examId);
            return ResponseEntity.ok(examId);
        } catch (Exception e) {
            logger.error("Error creating exam: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating exam: " + e.getMessage());
        }
    }

    @PostMapping("/exams/{examId}/questions")
    public ResponseEntity<Void> addQuestion(@PathVariable int examId, @RequestBody Question question) {
        try {
            logger.info("Adding question to exam {}: {}", examId, question);
            question.setExamId(examId);
            teacherService.addQuestion(question);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error adding question: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding question: " + e.getMessage());
        }
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
        try {
            logger.info("Fetching questions for exam {}", examId);
            List<Question> questions = teacherService.getExamQuestions(examId);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            logger.error("Error fetching questions: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching questions: " + e.getMessage());
        }
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