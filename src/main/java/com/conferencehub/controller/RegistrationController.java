package com.conferencehub.controller;

import com.conferencehub.dto.*;
import com.conferencehub.entity.*;
import com.conferencehub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RegistrationController — conference registration management.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired private RegistrationRepository registrationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ConferenceRepository conferenceRepository;

    /** Register the currently logged-in user for a conference */
    @PostMapping("/register/{conferenceId}")
    public ResponseEntity<?> registerForConference(@PathVariable Long conferenceId,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Conference conf = conferenceRepository.findById(conferenceId)
                .orElse(null);
        if (conf == null) return ResponseEntity.badRequest()
                .body(new MessageResponseDTO("Conference not found."));

        if (registrationRepository.existsByUserIdAndConferenceId(user.getId(), conferenceId)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO("Already registered for this conference."));
        }

        // Check capacity
        if (conf.getMaxParticipants() != null) {
            long count = registrationRepository.countByConferenceId(conferenceId);
            if (count >= conf.getMaxParticipants()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponseDTO("Conference is at full capacity."));
            }
        }

        Registration reg = Registration.builder()
                .user(user)
                .conference(conf)
                .status(Registration.RegistrationStatus.CONFIRMED)
                .build();

        return ResponseEntity.ok(toDTO(registrationRepository.save(reg)));
    }

    /** Get all registrations for the current user */
    @GetMapping("/my")
    public List<RegistrationResponseDTO> getMyRegistrations(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return registrationRepository.findByUserId(user.getId())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    /** Cancel a registration */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelRegistration(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        Registration reg = registrationRepository.findById(id).orElse(null);
        if (reg == null) return ResponseEntity.notFound().build();

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Participants can only cancel their own; Admins can cancel any
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !reg.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body(new MessageResponseDTO("Access denied."));
        }

        reg.setStatus(Registration.RegistrationStatus.CANCELLED);
        registrationRepository.save(reg);
        return ResponseEntity.ok(new MessageResponseDTO("Registration cancelled."));
    }

    // ── Admin endpoints ──────────────────────────────────────

    /** All registrations for a conference (ADMIN) */
    @GetMapping("/conference/{conferenceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RegistrationResponseDTO> getByConference(@PathVariable Long conferenceId) {
        return registrationRepository.findByConferenceId(conferenceId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    /** All registrations in the system (ADMIN) */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RegistrationResponseDTO> getAllRegistrations() {
        return registrationRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Mapper
    private RegistrationResponseDTO toDTO(Registration r) {
        RegistrationResponseDTO dto = new RegistrationResponseDTO();
        dto.setId(r.getId());
        dto.setUserId(r.getUser().getId());
        dto.setUserName(r.getUser().getName());
        dto.setUserEmail(r.getUser().getEmail());
        dto.setConferenceId(r.getConference().getId());
        dto.setConferenceTitle(r.getConference().getTitle());
        dto.setStatus(r.getStatus());
        dto.setRegisteredAt(r.getRegisteredAt());
        return dto;
    }
}
