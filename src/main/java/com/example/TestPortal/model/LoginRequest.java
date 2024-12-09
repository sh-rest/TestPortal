package com.example.TestPortal.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
    private String userType;
} 