package com.conferencehub.controller;

import com.conferencehub.dto.MessageResponseDTO;
import com.conferencehub.entity.User;
import com.conferencehub.repository.UserRepository;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * UserProfileController — allows any authenticated user to view and update their own profile.
 * Endpoint: /api/profile
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    /** Get current user's profile */
    @GetMapping
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(toMap(user));
    }

    /** Update current user's name, institution, phone */
    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> updates,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updates.containsKey("name") && !updates.get("name").isBlank())
            user.setName(updates.get("name").trim());
        if (updates.containsKey("institution"))
            user.setInstitution(updates.get("institution").trim());
        if (updates.containsKey("phone"))
            user.setPhone(updates.get("phone").trim());

        userRepository.save(user);
        return ResponseEntity.ok(toMap(user));
    }

    /** Change password */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        String current  = body.get("currentPassword");
        String newPass  = body.get("newPassword");

        if (current == null || newPass == null || newPass.length() < 6) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO("New password must be at least 6 characters."));
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(current, user.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO("Current password is incorrect."));
        }

        user.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponseDTO("Password changed successfully."));
    }

    private Map<String, Object> toMap(User u) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", u.getId());
        m.put("name", u.getName());
        m.put("email", u.getEmail());
        m.put("role", u.getRole());
        m.put("institution", u.getInstitution());
        m.put("phone", u.getPhone());
        m.put("createdAt", u.getCreatedAt());
        return m;
    }
}
