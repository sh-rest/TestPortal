package com.example.TestPortal.service;

import com.example.TestPortal.model.Exam;
import com.example.TestPortal.repository.ExamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {
    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public void createExam(Exam exam) {
        examRepository.createExam(exam);
    }

    public Exam getExamById(int examId) {
        return examRepository.getExamById(examId);
    }

    public List<Exam> getExamsByCourse(int courseId) {
        return examRepository.getExamsByCourse(courseId);
    }
} 