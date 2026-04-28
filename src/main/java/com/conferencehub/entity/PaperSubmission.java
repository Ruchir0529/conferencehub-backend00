package com.conferencehub.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "paper_submissions")
public class PaperSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String abstractText;

    @Column(nullable = false)
    private String keywords;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by", nullable = false)
    private User submittedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime submittedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "paper", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    public enum SubmissionStatus {
        SUBMITTED, UNDER_REVIEW, ACCEPTED, REJECTED, REVISION_REQUIRED
    }

    // ── Constructors ──────────────────────────────────────────────
    public PaperSubmission() {}

    // ── Builder ───────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String title; private String abstractText;
        private String keywords; private String filePath; private String fileName;
        private String fileType; private SubmissionStatus status;
        private User submittedBy; private Conference conference;

        public Builder id(Long id)                       { this.id = id; return this; }
        public Builder title(String title)               { this.title = title; return this; }
        public Builder abstractText(String a)            { this.abstractText = a; return this; }
        public Builder keywords(String k)                { this.keywords = k; return this; }
        public Builder filePath(String f)                { this.filePath = f; return this; }
        public Builder fileName(String f)                { this.fileName = f; return this; }
        public Builder fileType(String f)                { this.fileType = f; return this; }
        public Builder status(SubmissionStatus s)        { this.status = s; return this; }
        public Builder submittedBy(User u)               { this.submittedBy = u; return this; }
        public Builder conference(Conference c)          { this.conference = c; return this; }

        public PaperSubmission build() {
            PaperSubmission p = new PaperSubmission();
            p.id = this.id; p.title = this.title; p.abstractText = this.abstractText;
            p.keywords = this.keywords; p.filePath = this.filePath; p.fileName = this.fileName;
            p.fileType = this.fileType; p.status = this.status;
            p.submittedBy = this.submittedBy; p.conference = this.conference;
            return p;
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────
    public Long getId()                                  { return id; }
    public void setId(Long id)                           { this.id = id; }
    public String getTitle()                             { return title; }
    public void setTitle(String title)                   { this.title = title; }
    public String getAbstractText()                      { return abstractText; }
    public void setAbstractText(String abstractText)     { this.abstractText = abstractText; }
    public String getKeywords()                          { return keywords; }
    public void setKeywords(String keywords)             { this.keywords = keywords; }
    public String getFilePath()                          { return filePath; }
    public void setFilePath(String filePath)             { this.filePath = filePath; }
    public String getFileName()                          { return fileName; }
    public void setFileName(String fileName)             { this.fileName = fileName; }
    public String getFileType()                          { return fileType; }
    public void setFileType(String fileType)             { this.fileType = fileType; }
    public SubmissionStatus getStatus()                  { return status; }
    public void setStatus(SubmissionStatus status)       { this.status = status; }
    public User getSubmittedBy()                         { return submittedBy; }
    public void setSubmittedBy(User submittedBy)         { this.submittedBy = submittedBy; }
    public Conference getConference()                    { return conference; }
    public void setConference(Conference conference)     { this.conference = conference; }
    public LocalDateTime getSubmittedAt()                { return submittedAt; }
    public void setSubmittedAt(LocalDateTime s)          { this.submittedAt = s; }
    public LocalDateTime getUpdatedAt()                  { return updatedAt; }
    public void setUpdatedAt(LocalDateTime u)            { this.updatedAt = u; }
    public List<Review> getReviews()                     { return reviews; }
    public void setReviews(List<Review> reviews)         { this.reviews = reviews; }
}
