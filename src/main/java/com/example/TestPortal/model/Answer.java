package com.example.TestPortal.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    private int answerId;
    private int submissionId;
    private int questionId;
    private String selectedOption;
    private boolean correct;
} 