package com.conferencehub.dto;

import com.conferencehub.entity.Registration;
import java.time.LocalDateTime;

public class RegistrationResponseDTO {

    private Long                           id;
    private Long                           userId;
    private String                         userName;
    private String                         userEmail;
    private Long                           conferenceId;
    private String                         conferenceTitle;
    private Registration.RegistrationStatus status;
    private LocalDateTime                  registeredAt;

    public RegistrationResponseDTO() {}

    public Long getId()                                                  { return id; }
    public void setId(Long id)                                           { this.id = id; }
    public Long getUserId()                                              { return userId; }
    public void setUserId(Long userId)                                   { this.userId = userId; }
    public String getUserName()                                          { return userName; }
    public void setUserName(String userName)                             { this.userName = userName; }
    public String getUserEmail()                                         { return userEmail; }
    public void setUserEmail(String userEmail)                           { this.userEmail = userEmail; }
    public Long getConferenceId()                                        { return conferenceId; }
    public void setConferenceId(Long conferenceId)                       { this.conferenceId = conferenceId; }
    public String getConferenceTitle()                                   { return conferenceTitle; }
    public void setConferenceTitle(String conferenceTitle)               { this.conferenceTitle = conferenceTitle; }
    public Registration.RegistrationStatus getStatus()                   { return status; }
    public void setStatus(Registration.RegistrationStatus status)        { this.status = status; }
    public LocalDateTime getRegisteredAt()                               { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt)              { this.registeredAt = registeredAt; }
}
