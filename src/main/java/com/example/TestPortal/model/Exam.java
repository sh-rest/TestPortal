package com.example.TestPortal.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
    private int examId;
    private String title;
    private String description;
    private LocalDateTime date;
    private int courseId;
    private int duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
} 