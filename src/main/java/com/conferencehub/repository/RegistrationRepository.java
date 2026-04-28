package com.conferencehub.repository;

import com.conferencehub.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByUserId(Long userId);
    List<Registration> findByConferenceId(Long conferenceId);
    Optional<Registration> findByUserIdAndConferenceId(Long userId, Long conferenceId);
    boolean existsByUserIdAndConferenceId(Long userId, Long conferenceId);
    long countByConferenceId(Long conferenceId);
}
