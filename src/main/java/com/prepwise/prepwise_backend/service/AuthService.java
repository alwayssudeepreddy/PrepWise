package com.prepwise.prepwise_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prepwise.prepwise_backend.dto.auth.AuthResponse;
import com.prepwise.prepwise_backend.dto.auth.LoginRequest;
import com.prepwise.prepwise_backend.dto.auth.RegisterRequest;
import com.prepwise.prepwise_backend.entity.Role;
import com.prepwise.prepwise_backend.entity.User;
import com.prepwise.prepwise_backend.repository.UserRepository;
import com.prepwise.prepwise_backend.security.JwtService;
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

    if (userRepository.existsByUsername(request.getUsername())) {
        throw new RuntimeException("Username already exists");
    }

    if (userRepository.existsByEmail(request.getEmail())) {
        throw new RuntimeException("Email already exists");
    }

    User user = User.builder()
            .username(request.getUsername())
            .fullName(request.getFullName())
            .email(request.getEmail())
            .password(encoder.encode(request.getPassword()))
            .role(Role.STUDENT)
            .build();

    userRepository.save(user);

     String jwtToken = jwtService.generateToken(user.getUsername(),user.getRole().name());

    return AuthResponse.builder()
            .message("User registered successfully")
            .username(user.getUsername())
            .role(user.getRole().name())
            .token(jwtToken)
            .build();
}
      
        
    

    public AuthResponse login(LoginRequest request) {

      

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
    System.out.println(user.getFullName()+" "+request.getEmail());
    
    if (!encoder.matches(request.getPassword(), user.getPassword())) {
        throw new RuntimeException("Invalid password");
    }
String jwtToken = jwtService.generateToken(user.getUsername(),user.getRole().name());
    return AuthResponse.builder()
            .message("User logged in successfully")
            .username(user.getUsername())
            .role(user.getRole().name())
            .token(jwtToken)
            .build();
        
    }

}