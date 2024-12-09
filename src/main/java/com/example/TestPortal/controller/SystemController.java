package com.example.TestPortal.controller;

import com.example.TestPortal.service.SystemService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system")
public class SystemController {
    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    @PostMapping("/calculateTotalScore/{submissionId}")
    public void calculateTotalScoreForSubmission(@PathVariable int submissionId) {
        systemService.calculateTotalScoreForSubmission(submissionId);
    }

    // Add other endpoints as needed
} 