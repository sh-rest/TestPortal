package com.example.TestPortal.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String userType;  // "STUDENT", "TEACHER", or "ADMIN"
    private int userId;
    private String redirectUrl;
} 