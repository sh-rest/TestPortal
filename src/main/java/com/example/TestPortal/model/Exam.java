package com.example.TestPortal.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
    private int examId;
    private String title;
    private String description;
    private Date date;
    private int courseId;
    private int duration;
    private Date startTime;
    private Date endTime;
} 