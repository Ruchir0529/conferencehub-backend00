package com.conferencehub.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class SessionRequestDTO {

    @NotBlank
    private String title;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    private String speaker;
    private String room;
    private String description;

    @NotNull
    private Long conferenceId;

    public SessionRequestDTO() {}

    public String getTitle()                             { return title; }
    public void setTitle(String title)                   { this.title = title; }
    public LocalDateTime getStartTime()                  { return startTime; }
    public void setStartTime(LocalDateTime startTime)    { this.startTime = startTime; }
    public LocalDateTime getEndTime()                    { return endTime; }
    public void setEndTime(LocalDateTime endTime)        { this.endTime = endTime; }
    public String getSpeaker()                           { return speaker; }
    public void setSpeaker(String speaker)               { this.speaker = speaker; }
    public String getRoom()                              { return room; }
    public void setRoom(String room)                     { this.room = room; }
    public String getDescription()                       { return description; }
    public void setDescription(String description)       { this.description = description; }
    public Long getConferenceId()                        { return conferenceId; }
    public void setConferenceId(Long conferenceId)       { this.conferenceId = conferenceId; }
}
