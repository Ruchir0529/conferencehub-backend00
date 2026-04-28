package com.conferencehub.dto;

import com.conferencehub.entity.PaperSubmission;
import java.time.LocalDateTime;

public class PaperResponseDTO {

    private Long                           id;
    private String                         title;
    private String                         abstractText;
    private String                         keywords;
    private String                         fileName;
    private String                         fileType;
    private PaperSubmission.SubmissionStatus status;
    private Long                           submittedById;
    private String                         submittedByName;
    private Long                           conferenceId;
    private String                         conferenceTitle;
    private LocalDateTime                  submittedAt;

    public PaperResponseDTO() {}

    public Long getId()                                              { return id; }
    public void setId(Long id)                                       { this.id = id; }
    public String getTitle()                                         { return title; }
    public void setTitle(String title)                               { this.title = title; }
    public String getAbstractText()                                  { return abstractText; }
    public void setAbstractText(String abstractText)                 { this.abstractText = abstractText; }
    public String getKeywords()                                      { return keywords; }
    public void setKeywords(String keywords)                         { this.keywords = keywords; }
    public String getFileName()                                      { return fileName; }
    public void setFileName(String fileName)                         { this.fileName = fileName; }
    public String getFileType()                                      { return fileType; }
    public void setFileType(String fileType)                         { this.fileType = fileType; }
    public PaperSubmission.SubmissionStatus getStatus()              { return status; }
    public void setStatus(PaperSubmission.SubmissionStatus status)   { this.status = status; }
    public Long getSubmittedById()                                   { return submittedById; }
    public void setSubmittedById(Long submittedById)                 { this.submittedById = submittedById; }
    public String getSubmittedByName()                               { return submittedByName; }
    public void setSubmittedByName(String submittedByName)           { this.submittedByName = submittedByName; }
    public Long getConferenceId()                                    { return conferenceId; }
    public void setConferenceId(Long conferenceId)                   { this.conferenceId = conferenceId; }
    public String getConferenceTitle()                               { return conferenceTitle; }
    public void setConferenceTitle(String conferenceTitle)           { this.conferenceTitle = conferenceTitle; }
    public LocalDateTime getSubmittedAt()                            { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt)            { this.submittedAt = submittedAt; }
}
