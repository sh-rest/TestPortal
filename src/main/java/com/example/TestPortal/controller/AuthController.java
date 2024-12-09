package com.example.TestPortal.controller;

import com.example.TestPortal.model.LoginRequest;
import com.example.TestPortal.model.LoginResponse;
import com.example.TestPortal.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }
} 