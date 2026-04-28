package com.conferencehub.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paper_id", nullable = false)
    private PaperSubmission paper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column
    private Integer score;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewDecision decision;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus reviewStatus;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime assignedAt;

    @Column
    private LocalDateTime completedAt;

    public enum ReviewDecision { ACCEPT, REJECT, REVISION_REQUIRED, PENDING }
    public enum ReviewStatus   { ASSIGNED, IN_PROGRESS, COMPLETED }

    // ── Constructors ──────────────────────────────────────────────
    public Review() {}

    // ── Builder ───────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private PaperSubmission paper; private User reviewer;
        private String comments; private Integer score;
        private ReviewDecision decision; private ReviewStatus reviewStatus;

        public Builder id(Long id)                       { this.id = id; return this; }
        public Builder paper(PaperSubmission p)          { this.paper = p; return this; }
        public Builder reviewer(User u)                  { this.reviewer = u; return this; }
        public Builder comments(String c)                { this.comments = c; return this; }
        public Builder score(Integer s)                  { this.score = s; return this; }
        public Builder decision(ReviewDecision d)        { this.decision = d; return this; }
        public Builder reviewStatus(ReviewStatus s)      { this.reviewStatus = s; return this; }

        public Review build() {
            Review r = new Review();
            r.id = this.id; r.paper = this.paper; r.reviewer = this.reviewer;
            r.comments = this.comments; r.score = this.score;
            r.decision = this.decision; r.reviewStatus = this.reviewStatus;
            return r;
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────
    public Long getId()                                  { return id; }
    public void setId(Long id)                           { this.id = id; }
    public PaperSubmission getPaper()                    { return paper; }
    public void setPaper(PaperSubmission paper)          { this.paper = paper; }
    public User getReviewer()                            { return reviewer; }
    public void setReviewer(User reviewer)               { this.reviewer = reviewer; }
    public String getComments()                          { return comments; }
    public void setComments(String comments)             { this.comments = comments; }
    public Integer getScore()                            { return score; }
    public void setScore(Integer score)                  { this.score = score; }
    public ReviewDecision getDecision()                  { return decision; }
    public void setDecision(ReviewDecision decision)     { this.decision = decision; }
    public ReviewStatus getReviewStatus()                { return reviewStatus; }
    public void setReviewStatus(ReviewStatus reviewStatus){ this.reviewStatus = reviewStatus; }
    public LocalDateTime getAssignedAt()                 { return assignedAt; }
    public void setAssignedAt(LocalDateTime a)           { this.assignedAt = a; }
    public LocalDateTime getCompletedAt()                { return completedAt; }
    public void setCompletedAt(LocalDateTime c)          { this.completedAt = c; }
}
