package com.conferencehub.repository;

import com.conferencehub.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    List<Conference> findByStatus(Conference.ConferenceStatus status);
    List<Conference> findByStartDateAfterOrderByStartDateAsc(LocalDate date);
    
    @Query("SELECT c FROM Conference c WHERE c.status IN ('UPCOMING','ACTIVE') ORDER BY c.startDate ASC")
    List<Conference> findActiveAndUpcoming();
}
