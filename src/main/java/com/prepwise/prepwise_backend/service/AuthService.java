package com.prepwise.prepwise_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prepwise.prepwise_backend.dto.auth.AuthResponse;
import com.prepwise.prepwise_backend.dto.auth.LoginRequest;
import com.prepwise.prepwise_backend.dto.auth.RegisterRequest;
import com.prepwise.prepwise_backend.entity.Role;
import com.prepwise.prepwise_backend.entity.User;
import com.prepwise.prepwise_backend.exception.DuplicateResourceException;
import com.prepwise.prepwise_backend.exception.InvalidCredentialsException;
import com.prepwise.prepwise_backend.repository.UserRepository;
import com.prepwise.prepwise_backend.security.JwtService;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

    if (userRepository.existsByUsername(request.getUsername())) {
        throw new DuplicateResourceException("Username already exists");
    }

    if (userRepository.existsByEmail(request.getEmail())) {
        throw new DuplicateResourceException("Email already exists");
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

    // Use the same message for "unknown email" and "wrong password" to avoid user enumeration.
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

    if (!encoder.matches(request.getPassword(), user.getPassword())) {
        throw new InvalidCredentialsException("Invalid email or password");
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