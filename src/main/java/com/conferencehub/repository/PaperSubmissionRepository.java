package com.conferencehub.repository;

import com.conferencehub.entity.PaperSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaperSubmissionRepository extends JpaRepository<PaperSubmission, Long> {
    List<PaperSubmission> findBySubmittedById(Long userId);
    List<PaperSubmission> findByConferenceId(Long conferenceId);
    List<PaperSubmission> findByStatus(PaperSubmission.SubmissionStatus status);
    List<PaperSubmission> findBySubmittedByIdAndConferenceId(Long userId, Long conferenceId);
}
