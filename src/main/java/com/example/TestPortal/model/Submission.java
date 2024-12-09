package com.example.TestPortal.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    private int submissionId;
    private int studentId;
    private int examId;
    private double totalScore;
    private Date submissionDate;
} 