package com.example.TestPortal.model;

import lombok.Data;

@Data
public class Admin {
    private int adminId;
    private String name;
    private String email;
    private String password;
}