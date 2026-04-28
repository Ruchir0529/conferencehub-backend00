package com.conferencehub.dto;

import com.conferencehub.entity.Conference;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ConferenceRequestDTO {

    @NotBlank
    private String title;
    private String description;

    @NotBlank
    private String venue;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private LocalDate submissionDeadline;
    private Integer   maxParticipants;
    private String    topic;
    private String    websiteUrl;
    private Conference.ConferenceStatus status;

    public ConferenceRequestDTO() {}

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
    public String getTopic()                                     { return topic; }
    public void setTopic(String topic)                           { this.topic = topic; }
    public String getWebsiteUrl()                                { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl)                 { this.websiteUrl = websiteUrl; }
    public Conference.ConferenceStatus getStatus()               { return status; }
    public void setStatus(Conference.ConferenceStatus status)    { this.status = status; }
}
