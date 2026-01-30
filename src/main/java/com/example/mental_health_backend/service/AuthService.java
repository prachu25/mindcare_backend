package com.example.mental_health_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mental_health_backend.dto.LoginRequest;
import com.example.mental_health_backend.dto.RegisterRequest;
import com.example.mental_health_backend.entity.User;
import com.example.mental_health_backend.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    public String register(RegisterRequest request) {

        // 1️ Service-level check
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists";
        }

        String password = request.getPassword();

        // PASSWORD MINIMUM 6 CHAR CHECK (NO ERROR)
        if (password == null || password.trim().length() < 6) {
            return "Password must be at least 6 characters long";
        }

        try {
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(password);
            user.setRole("USER");

            // UNIQUE constraint (EMAIL)
            userRepo.save(user);
            return "User registered successfully";

        } catch (Exception e) {
            // 2️ DB-level safety net
            return "Email already exists";
        }
    }

    // LOGIN (PLAIN PASSWORD MATCH)
    public User login(LoginRequest request) {

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

}
