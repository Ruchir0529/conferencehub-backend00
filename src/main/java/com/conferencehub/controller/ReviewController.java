package com.conferencehub.controller;

import com.conferencehub.dto.*;
import com.conferencehub.entity.*;
import com.conferencehub.repository.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ReviewController — assign reviewers and record review outcomes.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private PaperSubmissionRepository paperRepo;
    @Autowired private UserRepository userRepository;

    /**
     * Assign a reviewer to a paper (ADMIN).
     * Creates a review record in ASSIGNED state.
     */
    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignReviewer(@Valid @RequestBody ReviewRequestDTO req) {
        PaperSubmission paper = paperRepo.findById(req.getPaperId()).orElse(null);
        if (paper == null) return ResponseEntity.badRequest()
                .body(new MessageResponseDTO("Paper not found."));

        User reviewer = userRepository.findById(req.getReviewerId()).orElse(null);
        if (reviewer == null) return ResponseEntity.badRequest()
                .body(new MessageResponseDTO("Reviewer not found."));

        if (reviewRepository.existsByPaperIdAndReviewerId(req.getPaperId(), req.getReviewerId())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO("Reviewer already assigned to this paper."));
        }

        Review review = Review.builder()
                .paper(paper)
                .reviewer(reviewer)
                .decision(Review.ReviewDecision.PENDING)
                .reviewStatus(Review.ReviewStatus.ASSIGNED)
                .build();

        // Mark paper as UNDER_REVIEW
        paper.setStatus(PaperSubmission.SubmissionStatus.UNDER_REVIEW);
        paperRepo.save(paper);

        return ResponseEntity.ok(toDTO(reviewRepository.save(review)));
    }

    /**
     * Reviewer submits their review (any authenticated user who is the assigned reviewer).
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> submitReview(@PathVariable Long id,
                                           @RequestBody ReviewRequestDTO req,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return ResponseEntity.notFound().build();

        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Ensure only the assigned reviewer (or admin) can update
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !review.getReviewer().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).body(new MessageResponseDTO("Access denied."));
        }

        review.setComments(req.getComments());
        review.setScore(req.getScore());
        review.setDecision(req.getDecision() != null ? req.getDecision() : Review.ReviewDecision.PENDING);
        review.setReviewStatus(Review.ReviewStatus.COMPLETED);
        review.setCompletedAt(LocalDateTime.now());

        // Propagate decision to paper status
        if (req.getDecision() != null) {
            switch (req.getDecision()) {
                case ACCEPT -> review.getPaper().setStatus(PaperSubmission.SubmissionStatus.ACCEPTED);
                case REJECT -> review.getPaper().setStatus(PaperSubmission.SubmissionStatus.REJECTED);
                case REVISION_REQUIRED -> review.getPaper()
                        .setStatus(PaperSubmission.SubmissionStatus.REVISION_REQUIRED);
                default -> {}
            }
            paperRepo.save(review.getPaper());
        }

        return ResponseEntity.ok(toDTO(reviewRepository.save(review)));
    }

    /** All reviews for a paper */
    @GetMapping("/paper/{paperId}")
    public List<ReviewResponseDTO> getReviewsByPaper(@PathVariable Long paperId) {
        return reviewRepository.findByPaperId(paperId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    /** Reviews assigned to the current reviewer */
    @GetMapping("/my")
    public List<ReviewResponseDTO> getMyReviews(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return reviewRepository.findByReviewerId(user.getId())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    /** All reviews (ADMIN) */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ReviewResponseDTO> getAllReviews() {
        return reviewRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Mapper
    private ReviewResponseDTO toDTO(Review r) {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(r.getId());
        dto.setPaperId(r.getPaper().getId());
        dto.setPaperTitle(r.getPaper().getTitle());
        dto.setReviewerId(r.getReviewer().getId());
        dto.setReviewerName(r.getReviewer().getName());
        dto.setComments(r.getComments());
        dto.setScore(r.getScore());
        dto.setDecision(r.getDecision());
        dto.setReviewStatus(r.getReviewStatus());
        dto.setAssignedAt(r.getAssignedAt());
        dto.setCompletedAt(r.getCompletedAt());
        return dto;
    }
}
