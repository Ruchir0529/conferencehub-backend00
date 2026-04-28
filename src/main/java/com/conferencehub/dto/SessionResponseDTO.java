package com.conferencehub.dto;

import java.time.LocalDateTime;

public class SessionResponseDTO {

    private Long          id;
    private String        title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String        speaker;
    private String        room;
    private String        description;
    private Long          conferenceId;
    private String        conferenceTitle;

    public SessionResponseDTO() {}

    public Long getId()                                  { return id; }
    public void setId(Long id)                           { this.id = id; }
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
    public String getConferenceTitle()                   { return conferenceTitle; }
    public void setConferenceTitle(String conferenceTitle){ this.conferenceTitle = conferenceTitle; }
}
