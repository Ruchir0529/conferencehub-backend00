package com.conferencehub.dto;

import com.conferencehub.entity.Review;
import jakarta.validation.constraints.*;

public class ReviewRequestDTO {

    @NotNull
    private Long paperId;

    @NotNull
    private Long reviewerId;

    private String comments;

    @Min(1) @Max(10)
    private Integer score;

    private Review.ReviewDecision decision;
    private Review.ReviewStatus   reviewStatus;

    public ReviewRequestDTO() {}

    public Long getPaperId()                                         { return paperId; }
    public void setPaperId(Long paperId)                             { this.paperId = paperId; }
    public Long getReviewerId()                                      { return reviewerId; }
    public void setReviewerId(Long reviewerId)                       { this.reviewerId = reviewerId; }
    public String getComments()                                      { return comments; }
    public void setComments(String comments)                         { this.comments = comments; }
    public Integer getScore()                                        { return score; }
    public void setScore(Integer score)                              { this.score = score; }
    public Review.ReviewDecision getDecision()                       { return decision; }
    public void setDecision(Review.ReviewDecision decision)          { this.decision = decision; }
    public Review.ReviewStatus getReviewStatus()                     { return reviewStatus; }
    public void setReviewStatus(Review.ReviewStatus reviewStatus)    { this.reviewStatus = reviewStatus; }
}
