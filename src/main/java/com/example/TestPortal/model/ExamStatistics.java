package com.example.TestPortal.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamStatistics {
    private double averageScore;
    private double highestScore;
    private double lowestScore;
    private int totalSubmissions;
} 