package com.conferencehub.dto;

import com.conferencehub.entity.Conference;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConferenceResponseDTO {

    private Long   id;
    private String title;
    private String description;
    private String venue;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate submissionDeadline;
    private Integer   maxParticipants;
    private String    topic;
    private String    websiteUrl;
    private Conference.ConferenceStatus status;
    private LocalDateTime createdAt;
    private long registrationCount;

    public ConferenceResponseDTO() {}

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
    public void setMaxParticipants(Integer m)                    { this.maxParticipants = m; }
    public String getTopic()                                     { return topic; }
    public void setTopic(String topic)                           { this.topic = topic; }
    public String getWebsiteUrl()                                { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl)                 { this.websiteUrl = websiteUrl; }
    public Conference.ConferenceStatus getStatus()               { return status; }
    public void setStatus(Conference.ConferenceStatus status)    { this.status = status; }
    public LocalDateTime getCreatedAt()                          { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt)            { this.createdAt = createdAt; }
    public long getRegistrationCount()                           { return registrationCount; }
    public void setRegistrationCount(long registrationCount)     { this.registrationCount = registrationCount; }
}
