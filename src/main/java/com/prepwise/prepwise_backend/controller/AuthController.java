package com.prepwise.prepwise_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prepwise.prepwise_backend.dto.auth.AuthResponse;
import com.prepwise.prepwise_backend.dto.auth.LoginRequest;
import com.prepwise.prepwise_backend.dto.auth.RegisterRequest;
import com.prepwise.prepwise_backend.service.AuthService;


@RestController
public class AuthController {

    @Autowired
    AuthService auth;


    @GetMapping("/test")
    public String test() {
        return "Security Working!";
    }

   
    @PostMapping("/api/auth/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {

        return auth.register(request);
    }

    @PostMapping("/api/auth/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        
        return auth.login(request);

        
    }
    
}