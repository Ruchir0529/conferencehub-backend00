package com.conferencehub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column
    private String speaker;

    @Column
    private String room;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    // ── Constructors ──────────────────────────────────────────────
    public Session() {}

    // ── Builder ───────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String title; private LocalDateTime startTime;
        private LocalDateTime endTime; private String speaker;
        private String room; private String description; private Conference conference;

        public Builder id(Long id)                       { this.id = id; return this; }
        public Builder title(String title)               { this.title = title; return this; }
        public Builder startTime(LocalDateTime t)        { this.startTime = t; return this; }
        public Builder endTime(LocalDateTime t)          { this.endTime = t; return this; }
        public Builder speaker(String speaker)           { this.speaker = speaker; return this; }
        public Builder room(String room)                 { this.room = room; return this; }
        public Builder description(String d)             { this.description = d; return this; }
        public Builder conference(Conference c)          { this.conference = c; return this; }

        public Session build() {
            Session s = new Session();
            s.id = this.id; s.title = this.title; s.startTime = this.startTime;
            s.endTime = this.endTime; s.speaker = this.speaker; s.room = this.room;
            s.description = this.description; s.conference = this.conference;
            return s;
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────
    public Long getId()                              { return id; }
    public void setId(Long id)                       { this.id = id; }
    public String getTitle()                         { return title; }
    public void setTitle(String title)               { this.title = title; }
    public LocalDateTime getStartTime()              { return startTime; }
    public void setStartTime(LocalDateTime startTime){ this.startTime = startTime; }
    public LocalDateTime getEndTime()                { return endTime; }
    public void setEndTime(LocalDateTime endTime)    { this.endTime = endTime; }
    public String getSpeaker()                       { return speaker; }
    public void setSpeaker(String speaker)           { this.speaker = speaker; }
    public String getRoom()                          { return room; }
    public void setRoom(String room)                 { this.room = room; }
    public String getDescription()                   { return description; }
    public void setDescription(String description)   { this.description = description; }
    public Conference getConference()                { return conference; }
    public void setConference(Conference conference) { this.conference = conference; }
}
