package com.conferencehub.controller;

import com.conferencehub.dto.*;
import com.conferencehub.entity.Conference;
import com.conferencehub.repository.ConferenceRepository;
import com.conferencehub.repository.RegistrationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ConferenceController — CRUD for conferences.
 * GET endpoints are public; POST/PUT/DELETE require ADMIN role.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/conferences")
public class ConferenceController {

    @Autowired private ConferenceRepository conferenceRepository;
    @Autowired private RegistrationRepository registrationRepository;

    // ── Public ──────────────────────────────────────────────

    /** List all conferences */
    @GetMapping
    public List<ConferenceResponseDTO> getAllConferences() {
        return conferenceRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /** List only upcoming and active conferences */
    @GetMapping("/active")
    public List<ConferenceResponseDTO> getActiveConferences() {
        return conferenceRepository.findActiveAndUpcoming().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /** Get a single conference by ID */
    @GetMapping("/{id}")
    public ResponseEntity<ConferenceResponseDTO> getConference(@PathVariable Long id) {
        return conferenceRepository.findById(id)
                .map(c -> ResponseEntity.ok(toDTO(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ── Admin only ──────────────────────────────────────────

    /** Create a new conference */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConferenceResponseDTO> createConference(@Valid @RequestBody ConferenceRequestDTO req) {
        Conference conf = Conference.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .venue(req.getVenue())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .submissionDeadline(req.getSubmissionDeadline())
                .maxParticipants(req.getMaxParticipants())
                .topic(req.getTopic())
                .websiteUrl(req.getWebsiteUrl())
                .status(req.getStatus() != null ? req.getStatus() : Conference.ConferenceStatus.UPCOMING)
                .build();
        Conference saved = conferenceRepository.save(conf);
        return ResponseEntity.ok(toDTO(saved));
    }

    /** Update an existing conference */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConferenceResponseDTO> updateConference(@PathVariable Long id,
                                                                   @Valid @RequestBody ConferenceRequestDTO req) {
        return conferenceRepository.findById(id).map(conf -> {
            conf.setTitle(req.getTitle());
            conf.setDescription(req.getDescription());
            conf.setVenue(req.getVenue());
            conf.setStartDate(req.getStartDate());
            conf.setEndDate(req.getEndDate());
            conf.setSubmissionDeadline(req.getSubmissionDeadline());
            conf.setMaxParticipants(req.getMaxParticipants());
            conf.setTopic(req.getTopic());
            conf.setWebsiteUrl(req.getWebsiteUrl());
            if (req.getStatus() != null) conf.setStatus(req.getStatus());
            return ResponseEntity.ok(toDTO(conferenceRepository.save(conf)));
        }).orElse(ResponseEntity.notFound().build());
    }

    /** Update only the status of a conference */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConferenceResponseDTO> updateStatus(@PathVariable Long id,
                                                               @RequestParam Conference.ConferenceStatus status) {
        return conferenceRepository.findById(id).map(conf -> {
            conf.setStatus(status);
            return ResponseEntity.ok(toDTO(conferenceRepository.save(conf)));
        }).orElse(ResponseEntity.notFound().build());
    }

    /** Delete a conference */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponseDTO> deleteConference(@PathVariable Long id) {
        if (!conferenceRepository.existsById(id)) return ResponseEntity.notFound().build();
        conferenceRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponseDTO("Conference deleted successfully."));
    }

    // ── Mapper ──────────────────────────────────────────────

    private ConferenceResponseDTO toDTO(Conference c) {
        ConferenceResponseDTO dto = new ConferenceResponseDTO();
        dto.setId(c.getId());
        dto.setTitle(c.getTitle());
        dto.setDescription(c.getDescription());
        dto.setVenue(c.getVenue());
        dto.setStartDate(c.getStartDate());
        dto.setEndDate(c.getEndDate());
        dto.setSubmissionDeadline(c.getSubmissionDeadline());
        dto.setMaxParticipants(c.getMaxParticipants());
        dto.setTopic(c.getTopic());
        dto.setWebsiteUrl(c.getWebsiteUrl());
        dto.setStatus(c.getStatus());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setRegistrationCount(registrationRepository.countByConferenceId(c.getId()));
        return dto;
    }
}
