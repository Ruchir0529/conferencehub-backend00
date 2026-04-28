package com.conferencehub.controller;

import com.conferencehub.dto.*;
import com.conferencehub.entity.*;
import com.conferencehub.repository.*;
import com.conferencehub.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PaperSubmissionController — paper upload, retrieval, and status management.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/papers")
public class PaperSubmissionController {

    @Autowired private PaperSubmissionRepository paperRepo;
    @Autowired private UserRepository userRepository;
    @Autowired private ConferenceRepository conferenceRepository;
    @Autowired private FileStorageService fileStorageService;

    /**
     * Upload a paper (authenticated participants and admins).
     * Accepts multipart form with file + metadata fields.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitPaper(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("abstractText") String abstractText,
            @RequestParam("keywords") String keywords,
            @RequestParam("conferenceId") Long conferenceId,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Conference conf = conferenceRepository.findById(conferenceId).orElse(null);
        if (conf == null) return ResponseEntity.badRequest()
                .body(new MessageResponseDTO("Conference not found."));

        String storedFilename = fileStorageService.storeFile(file);
        String fileType = file.getContentType() != null &&
                file.getContentType().contains("pdf") ? "PDF" : "JPEG";

        PaperSubmission paper = PaperSubmission.builder()
                .title(title)
                .abstractText(abstractText)
                .keywords(keywords)
                .filePath(storedFilename)
                .fileName(file.getOriginalFilename())
                .fileType(fileType)
                .status(PaperSubmission.SubmissionStatus.SUBMITTED)
                .submittedBy(user)
                .conference(conf)
                .build();

        return ResponseEntity.ok(toDTO(paperRepo.save(paper)));
    }

    /** Get all papers submitted by the current user */
    @GetMapping("/my")
    public List<PaperResponseDTO> getMyPapers(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return paperRepo.findBySubmittedById(user.getId())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    /** Get a single paper by ID */
    @GetMapping("/{id}")
    public ResponseEntity<PaperResponseDTO> getPaper(@PathVariable Long id) {
        return paperRepo.findById(id)
                .map(p -> ResponseEntity.ok(toDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    /** Download the paper file */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadPaper(@PathVariable Long id) {
        PaperSubmission paper = paperRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Paper not found"));
        try {
            Path filePath = fileStorageService.loadFile(paper.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) return ResponseEntity.notFound().build();

            String contentType = paper.getFileType().equals("PDF")
                    ? MediaType.APPLICATION_PDF_VALUE : MediaType.IMAGE_JPEG_VALUE;
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + paper.getFileName() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(500).build();
        }
    }

    // ── Admin endpoints ──────────────────────────────────────

    /** All submissions (ADMIN) */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PaperResponseDTO> getAllPapers() {
        return paperRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    /** Submissions for a specific conference (ADMIN) */
    @GetMapping("/conference/{conferenceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PaperResponseDTO> getPapersByConference(@PathVariable Long conferenceId) {
        return paperRepo.findByConferenceId(conferenceId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    /** Update submission status (ADMIN) */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                           @RequestParam PaperSubmission.SubmissionStatus status) {
        return paperRepo.findById(id).map(p -> {
            p.setStatus(status);
            return ResponseEntity.ok(toDTO(paperRepo.save(p)));
        }).orElse(ResponseEntity.notFound().build());
    }

    /** Delete a submission (ADMIN) */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponseDTO> deletePaper(@PathVariable Long id) {
        PaperSubmission paper = paperRepo.findById(id).orElse(null);
        if (paper == null) return ResponseEntity.notFound().build();
        fileStorageService.deleteFile(paper.getFilePath());
        paperRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponseDTO("Paper deleted."));
    }

    // Mapper
    private PaperResponseDTO toDTO(PaperSubmission p) {
        PaperResponseDTO dto = new PaperResponseDTO();
        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setAbstractText(p.getAbstractText());
        dto.setKeywords(p.getKeywords());
        dto.setFileName(p.getFileName());
        dto.setFileType(p.getFileType());
        dto.setStatus(p.getStatus());
        dto.setSubmittedById(p.getSubmittedBy().getId());
        dto.setSubmittedByName(p.getSubmittedBy().getName());
        dto.setConferenceId(p.getConference().getId());
        dto.setConferenceTitle(p.getConference().getTitle());
        dto.setSubmittedAt(p.getSubmittedAt());
        return dto;
    }
}
