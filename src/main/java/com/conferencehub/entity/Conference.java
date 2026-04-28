package com.conferencehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "conferences")
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String venue;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column
    private LocalDate submissionDeadline;

    @Column
    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConferenceStatus status;

    @Column
    private String topic;

    @Column
    private String websiteUrl;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Session> sessions;

    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Registration> registrations;

    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaperSubmission> paperSubmissions;

    public enum ConferenceStatus {
        UPCOMING, ACTIVE, COMPLETED, CANCELLED
    }

    // ── Constructors ──────────────────────────────────────────────
    public Conference() {}

    // ── Builder ───────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String title; private String description;
        private String venue; private LocalDate startDate; private LocalDate endDate;
        private LocalDate submissionDeadline; private Integer maxParticipants;
        private ConferenceStatus status; private String topic; private String websiteUrl;

        public Builder id(Long id)                               { this.id = id; return this; }
        public Builder title(String title)                       { this.title = title; return this; }
        public Builder description(String d)                     { this.description = d; return this; }
        public Builder venue(String venue)                       { this.venue = venue; return this; }
        public Builder startDate(LocalDate d)                    { this.startDate = d; return this; }
        public Builder endDate(LocalDate d)                      { this.endDate = d; return this; }
        public Builder submissionDeadline(LocalDate d)           { this.submissionDeadline = d; return this; }
        public Builder maxParticipants(Integer m)                { this.maxParticipants = m; return this; }
        public Builder status(ConferenceStatus s)                { this.status = s; return this; }
        public Builder topic(String topic)                       { this.topic = topic; return this; }
        public Builder websiteUrl(String url)                    { this.websiteUrl = url; return this; }

        public Conference build() {
            Conference c = new Conference();
            c.id = this.id; c.title = this.title; c.description = this.description;
            c.venue = this.venue; c.startDate = this.startDate; c.endDate = this.endDate;
            c.submissionDeadline = this.submissionDeadline; c.maxParticipants = this.maxParticipants;
            c.status = this.status; c.topic = this.topic; c.websiteUrl = this.websiteUrl;
            return c;
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────
    public Long getId()                                          { return id; }
    public void setId(Long id)                                   { this.id = id; }
    public String getTitle()                                     { return title; }
    public void setTitle(String title)                           { this.title = title; }
    public String getDescription()                               { return description; }
    public void setDescription(String description)               { this.description = description; }
    public String getVenue()                                     { return venue; }
    public void setVenue(String venue)                           { this.venue = venue; }
    public LocalDate getStartDate()                              { return startDate; }
    public void setStartDate(LocalDate startDate)                { this.startDate = startDate; }
    public LocalDate getEndDate()                                { return endDate; }
    public void setEndDate(LocalDate endDate)                    { this.endDate = endDate; }
    public LocalDate getSubmissionDeadline()                     { return submissionDeadline; }
    public void setSubmissionDeadline(LocalDate d)               { this.submissionDeadline = d; }
    public Integer getMaxParticipants()                          { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants)      { this.maxParticipants = maxParticipants; }
    public ConferenceStatus getStatus()                          { return status; }
    public void setStatus(ConferenceStatus status)               { this.status = status; }
    public String getTopic()                                     { return topic; }
    public void setTopic(String topic)                           { this.topic = topic; }
    public String getWebsiteUrl()                                { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl)                 { this.websiteUrl = websiteUrl; }
    public LocalDateTime getCreatedAt()                          { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt)            { this.createdAt = createdAt; }
    public List<Session> getSessions()                           { return sessions; }
    public void setSessions(List<Session> sessions)              { this.sessions = sessions; }
    public List<Registration> getRegistrations()                 { return registrations; }
    public void setRegistrations(List<Registration> r)           { this.registrations = r; }
    public List<PaperSubmission> getPaperSubmissions()           { return paperSubmissions; }
    public void setPaperSubmissions(List<PaperSubmission> p)     { this.paperSubmissions = p; }
}
