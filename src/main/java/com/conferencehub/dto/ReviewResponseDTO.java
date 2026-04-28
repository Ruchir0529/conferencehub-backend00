package com.conferencehub.dto;

import com.conferencehub.entity.Review;
import java.time.LocalDateTime;

public class ReviewResponseDTO {

    private Long                   id;
    private Long                   paperId;
    private String                 paperTitle;
    private Long                   reviewerId;
    private String                 reviewerName;
    private String                 comments;
    private Integer                score;
    private Review.ReviewDecision  decision;
    private Review.ReviewStatus    reviewStatus;
    private LocalDateTime          assignedAt;
    private LocalDateTime          completedAt;

    public ReviewResponseDTO() {}

    public Long getId()                                              { return id; }
    public void setId(Long id)                                       { this.id = id; }
    public Long getPaperId()                                         { return paperId; }
    public void setPaperId(Long paperId)                             { this.paperId = paperId; }
    public String getPaperTitle()                                    { return paperTitle; }
    public void setPaperTitle(String paperTitle)                     { this.paperTitle = paperTitle; }
    public Long getReviewerId()                                      { return reviewerId; }
    public void setReviewerId(Long reviewerId)                       { this.reviewerId = reviewerId; }
    public String getReviewerName()                                  { return reviewerName; }
    public void setReviewerName(String reviewerName)                 { this.reviewerName = reviewerName; }
    public String getComments()                                      { return comments; }
    public void setComments(String comments)                         { this.comments = comments; }
    public Integer getScore()                                        { return score; }
    public void setScore(Integer score)                              { this.score = score; }
    public Review.ReviewDecision getDecision()                       { return decision; }
    public void setDecision(Review.ReviewDecision decision)          { this.decision = decision; }
    public Review.ReviewStatus getReviewStatus()                     { return reviewStatus; }
    public void setReviewStatus(Review.ReviewStatus reviewStatus)    { this.reviewStatus = reviewStatus; }
    public LocalDateTime getAssignedAt()                             { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt)              { this.assignedAt = assignedAt; }
    public LocalDateTime getCompletedAt()                            { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt)            { this.completedAt = completedAt; }
}
