package com.conferencehub.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "registrations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "conference_id"})
})
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime registeredAt;

    public enum RegistrationStatus {
        PENDING, CONFIRMED, CANCELLED
    }

    // ── Constructors ──────────────────────────────────────────────
    public Registration() {}

    // ── Builder ───────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private User user; private Conference conference;
        private RegistrationStatus status;

        public Builder id(Long id)                       { this.id = id; return this; }
        public Builder user(User user)                   { this.user = user; return this; }
        public Builder conference(Conference c)          { this.conference = c; return this; }
        public Builder status(RegistrationStatus s)      { this.status = s; return this; }

        public Registration build() {
            Registration r = new Registration();
            r.id = this.id; r.user = this.user;
            r.conference = this.conference; r.status = this.status;
            return r;
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────
    public Long getId()                                  { return id; }
    public void setId(Long id)                           { this.id = id; }
    public User getUser()                                { return user; }
    public void setUser(User user)                       { this.user = user; }
    public Conference getConference()                    { return conference; }
    public void setConference(Conference conference)     { this.conference = conference; }
    public RegistrationStatus getStatus()                { return status; }
    public void setStatus(RegistrationStatus status)     { this.status = status; }
    public LocalDateTime getRegisteredAt()               { return registeredAt; }
    public void setRegisteredAt(LocalDateTime r)         { this.registeredAt = r; }
}
