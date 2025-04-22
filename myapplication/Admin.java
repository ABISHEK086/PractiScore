package com.example.myapplication;

public class Admin {
    private String email;
    private String role;

    // Default constructor required for Firebase
    public Admin() {}

    public Admin(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
