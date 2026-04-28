package com.conferencehub.controller;

import com.conferencehub.dto.*;
import com.conferencehub.entity.User;
import com.conferencehub.repository.UserRepository;
import com.conferencehub.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController — handles user registration and login.
 * Public endpoints: POST /api/auth/signup, POST /api/auth/login
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired AuthenticationManager authenticationManager;
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired JwtUtils jwtUtils;

    /**
     * Register a new participant account.
     * Admins are created via a separate seeding / admin endpoint.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO("Error: Email is already registered."));
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.PARTICIPANT)
                .institution(request.getInstitution())
                .phone(request.getPhone())
                .build();

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponseDTO("User registered successfully! Please log in."));
    }

    /**
     * Authenticate and return JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(new JwtResponseDTO(
                jwt, user.getId(), user.getName(), user.getEmail(), user.getRole().name()
        ));
    }

    /**
     * Seed an admin account (disable in production or add extra security).
     */
    @PostMapping("/signup/admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignupRequestDTO request,
                                           @RequestParam String adminKey) {
        // Simple key-based guard — replace with a stronger mechanism in production
        if (!"ConferenceHubAdmin2024".equals(adminKey)) {
            return ResponseEntity.status(403).body(new MessageResponseDTO("Invalid admin key."));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO("Error: Email is already registered."));
        }
        User admin = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.ADMIN)
                .institution(request.getInstitution())
                .phone(request.getPhone())
                .build();

        userRepository.save(admin);
        return ResponseEntity.ok(new MessageResponseDTO("Admin account created successfully!"));
    }
}
