package com.example.TestPortal.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private int courseId;
    private String courseName;
    private String description;
    private int teacherId;
} 