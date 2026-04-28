package com.conferencehub.controller;

import com.conferencehub.dto.*;
import com.conferencehub.entity.Conference;
import com.conferencehub.entity.Session;
import com.conferencehub.repository.ConferenceRepository;
import com.conferencehub.repository.SessionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SessionController — manages conference sessions/slots.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired private SessionRepository sessionRepository;
    @Autowired private ConferenceRepository conferenceRepository;

    /** All sessions for a given conference (public) */
    @GetMapping("/conference/{conferenceId}")
    public List<SessionResponseDTO> getSessionsByConference(@PathVariable Long conferenceId) {
        return sessionRepository.findByConferenceIdOrderByStartTimeAsc(conferenceId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    /** Get a single session */
    @GetMapping("/{id}")
    public ResponseEntity<SessionResponseDTO> getSession(@PathVariable Long id) {
        return sessionRepository.findById(id)
                .map(s -> ResponseEntity.ok(toDTO(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    /** Create a session (ADMIN) */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSession(@Valid @RequestBody SessionRequestDTO req) {
        Conference conf = conferenceRepository.findById(req.getConferenceId())
                .orElse(null);
        if (conf == null) return ResponseEntity.badRequest()
                .body(new MessageResponseDTO("Conference not found."));

        Session session = Session.builder()
                .title(req.getTitle())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .speaker(req.getSpeaker())
                .room(req.getRoom())
                .description(req.getDescription())
                .conference(conf)
                .build();

        return ResponseEntity.ok(toDTO(sessionRepository.save(session)));
    }

    /** Update a session (ADMIN) */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSession(@PathVariable Long id,
                                            @Valid @RequestBody SessionRequestDTO req) {
        return sessionRepository.findById(id).map(s -> {
            s.setTitle(req.getTitle());
            s.setStartTime(req.getStartTime());
            s.setEndTime(req.getEndTime());
            s.setSpeaker(req.getSpeaker());
            s.setRoom(req.getRoom());
            s.setDescription(req.getDescription());
            return ResponseEntity.ok(toDTO(sessionRepository.save(s)));
        }).orElse(ResponseEntity.notFound().build());
    }

    /** Delete a session (ADMIN) */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponseDTO> deleteSession(@PathVariable Long id) {
        if (!sessionRepository.existsById(id)) return ResponseEntity.notFound().build();
        sessionRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponseDTO("Session deleted."));
    }

    // Mapper
    private SessionResponseDTO toDTO(Session s) {
        SessionResponseDTO dto = new SessionResponseDTO();
        dto.setId(s.getId());
        dto.setTitle(s.getTitle());
        dto.setStartTime(s.getStartTime());
        dto.setEndTime(s.getEndTime());
        dto.setSpeaker(s.getSpeaker());
        dto.setRoom(s.getRoom());
        dto.setDescription(s.getDescription());
        dto.setConferenceId(s.getConference().getId());
        dto.setConferenceTitle(s.getConference().getTitle());
        return dto;
    }
}
