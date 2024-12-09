package com.example.TestPortal.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    private int teacherId;
    private String name;
    private String email;
    private String password;
} 