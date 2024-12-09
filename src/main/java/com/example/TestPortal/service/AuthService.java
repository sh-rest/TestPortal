package com.example.TestPortal.service;

import com.example.TestPortal.model.LoginRequest;
import com.example.TestPortal.model.LoginResponse;
import com.example.TestPortal.repository.AuthRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LoginResponse authenticate(LoginRequest loginRequest) {
        Integer userId = authRepository.authenticateUser(
            loginRequest.getEmail(),
            loginRequest.getPassword(),
            loginRequest.getUserType()
        );

        if (userId == null) {
            throw new RuntimeException("Invalid credentials");
        }

        String redirectUrl = switch (loginRequest.getUserType().toUpperCase()) {
            case "ADMIN" -> "/admin/dashboard";
            case "TEACHER" -> "/teacher/dashboard";
            case "STUDENT" -> "/student/dashboard";
            default -> throw new IllegalArgumentException("Invalid user type");
        };

        return new LoginResponse(
            loginRequest.getUserType().toUpperCase(),
            userId,
            redirectUrl
        );
    }

    public void logout() {
        // Implement logout logic (e.g., invalidate token)
    }
} 