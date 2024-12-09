package com.example.TestPortal.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionStatistics {
    private int totalAttempts;
    private int correctAnswers;
    private double successRate;
} 