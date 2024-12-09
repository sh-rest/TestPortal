package com.example.TestPortal.controller;

import com.example.TestPortal.service.SystemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/view/submissions")
public class SubmissionViewController {
    private final SystemService systemService;

    public SubmissionViewController(SystemService systemService) {
        this.systemService = systemService;
    }

    @GetMapping("/{submissionId}/score")
    public ResponseEntity<Double> getSubmissionScore(@PathVariable int submissionId) {
        return ResponseEntity.ok(systemService.getSubmissionScore(submissionId));
    }
} 