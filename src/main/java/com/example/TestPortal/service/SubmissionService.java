package com.example.TestPortal.service;

import com.example.TestPortal.model.Submission;
import com.example.TestPortal.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;

    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public void createSubmission(Submission submission) {
        submissionRepository.createSubmission(submission);
    }

    public Submission getSubmissionById(int submissionId) {
        return submissionRepository.getSubmissionById(submissionId);
    }

    public List<Submission> getStudentSubmissions(int studentId) {
        return submissionRepository.getStudentSubmissions(studentId);
    }

    public List<Submission> getExamSubmissions(int examId) {
        return submissionRepository.getExamSubmissions(examId);
    }

    public void updateSubmissionScore(int submissionId, double totalScore) {
        submissionRepository.updateSubmissionScore(submissionId, totalScore);
    }
} 