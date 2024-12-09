package com.example.TestPortal.service;

import com.example.TestPortal.repository.SystemRepository;
import org.springframework.stereotype.Service;
import com.example.TestPortal.repository.SubmissionRepository;

@Service
public class SystemService {
    private final SystemRepository systemRepository;
    private final SubmissionRepository submissionRepository;

    public SystemService(SystemRepository systemRepository, SubmissionRepository submissionRepository) {
        this.systemRepository = systemRepository;
        this.submissionRepository = submissionRepository;
    }

    public void calculateTotalScoreForSubmission(int submissionId) {
        systemRepository.calculateTotalScoreForSubmission(submissionId);
    }

    public Double getSubmissionScore(int submissionId) {
        return submissionRepository.getSubmissionById(submissionId).getTotalScore();
    }

    // Add other business logic methods as needed
} 