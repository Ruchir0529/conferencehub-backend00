package com.conferencehub.controller;

import com.conferencehub.entity.User;
import com.conferencehub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AdminController — aggregated stats and user management for admins.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired private UserRepository userRepository;
    @Autowired private ConferenceRepository conferenceRepository;
    @Autowired private RegistrationRepository registrationRepository;
    @Autowired private PaperSubmissionRepository paperRepo;
    @Autowired private ReviewRepository reviewRepository;

    /**
     * Dashboard summary statistics.
     */
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalConferences", conferenceRepository.count());
        stats.put("activeConferences", conferenceRepository.findActiveAndUpcoming().size());
        stats.put("totalParticipants",
                userRepository.findByRole(User.Role.PARTICIPANT).size());
        stats.put("totalRegistrations", registrationRepository.count());
        stats.put("totalPapers", paperRepo.count());
        stats.put("pendingReviews",
                reviewRepository.findAll().stream()
                        .filter(r -> r.getReviewStatus() != com.conferencehub.entity.Review.ReviewStatus.COMPLETED)
                        .count());
        stats.put("acceptedPapers",
                paperRepo.findByStatus(com.conferencehub.entity.PaperSubmission.SubmissionStatus.ACCEPTED).size());
        stats.put("rejectedPapers",
                paperRepo.findByStatus(com.conferencehub.entity.PaperSubmission.SubmissionStatus.REJECTED).size());
        return stats;
    }

    /**
     * List all users (ADMIN).
     */
    @GetMapping("/users")
    public List<Map<String, Object>> getAllUsers() {
        return userRepository.findAll().stream().map(u -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", u.getId());
            m.put("name", u.getName());
            m.put("email", u.getEmail());
            m.put("role", u.getRole());
            m.put("institution", u.getInstitution());
            m.put("phone", u.getPhone());
            m.put("createdAt", u.getCreatedAt());
            return m;
        }).collect(Collectors.toList());
    }

    /**
     * Get a single user by ID.
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(u -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", u.getId());
                    m.put("name", u.getName());
                    m.put("email", u.getEmail());
                    m.put("role", u.getRole());
                    m.put("institution", u.getInstitution());
                    m.put("phone", u.getPhone());
                    m.put("createdAt", u.getCreatedAt());
                    return ResponseEntity.ok(m);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a user (ADMIN).
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id))
            return ResponseEntity.notFound().build();
        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully."));
    }

    /**
     * Update a user's role (ADMIN).
     */
    @PatchMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id,
                                             @RequestParam User.Role role) {
        return userRepository.findById(id).map(u -> {
            u.setRole(role);
            userRepository.save(u);
            return ResponseEntity.ok(Map.of("message", "Role updated to " + role));
        }).orElse(ResponseEntity.notFound().build());
    }
}
