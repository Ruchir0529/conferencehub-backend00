package com.conferencehub.config;

import com.conferencehub.entity.*;
import com.conferencehub.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired private UserRepository       userRepo;
    @Autowired private ConferenceRepository confRepo;
    @Autowired private SessionRepository    sessionRepo;
    @Autowired private PasswordEncoder      passwordEncoder;

    @Override
    public void run(String... args) {

        // ── Admin accounts ─────────────────────────────────────────────
        String[][] admins = {
            {"System Admin",  "admin@conferencehub.com",   "Admin@123"},
            {"Ruchir Admin",  "ruchir@conferencehub.com",  "Ruchir@123"},
            {"Examiner",      "examiner@conferencehub.com","Exam@123"},
        };
        for (String[] a : admins) {
            if (!userRepo.existsByEmail(a[1])) {
                userRepo.save(User.builder()
                        .name(a[0]).email(a[1])
                        .password(passwordEncoder.encode(a[2]))
                        .role(User.Role.ADMIN)
                        .institution("Conference Hub")
                        .build());
                log.info("✓ Admin seeded: {}", a[1]);
            }
        }

        // ── Sample participants ────────────────────────────────────────
        String[][] participants = {
            {"Dr. Alice Johnson",  "alice@university.edu",  "MIT"},
            {"Prof. Bob Williams", "bob@research.org",      "Stanford University"},
            {"Dr. Carol Martinez", "carol@tech.edu",        "Carnegie Mellon"},
            {"Mr. David Chen",     "david@postgrad.edu",    "UC Berkeley"},
        };
        for (String[] p : participants) {
            if (!userRepo.existsByEmail(p[1])) {
                userRepo.save(User.builder()
                        .name(p[0]).email(p[1])
                        .password(passwordEncoder.encode("Test@1234"))
                        .role(User.Role.PARTICIPANT)
                        .institution(p[2]).build());
            }
        }

        // ── Sample conferences & sessions ──────────────────────────────
        if (confRepo.count() == 0) {
            Conference aiConf = confRepo.save(Conference.builder()
                    .title("International Conference on Artificial Intelligence 2025")
                    .description("A premier forum for AI researchers and practitioners to present cutting-edge research and innovations in machine learning, deep learning, NLP, and robotics.")
                    .venue("Hyderabad International Convention Centre, Hyderabad")
                    .startDate(LocalDate.of(2025, 9, 15))
                    .endDate(LocalDate.of(2025, 9, 18))
                    .submissionDeadline(LocalDate.of(2025, 7, 1))
                    .maxParticipants(300)
                    .topic("Artificial Intelligence & Machine Learning")
                    .status(Conference.ConferenceStatus.UPCOMING)
                    .build());

            Conference secConf = confRepo.save(Conference.builder()
                    .title("World Symposium on Cybersecurity 2025")
                    .description("Bringing together security experts to address emerging threats, zero-trust architectures, and mitigation strategies in the era of AI-driven attacks.")
                    .venue("HITEX Exhibition Centre, Hyderabad")
                    .startDate(LocalDate.of(2025, 10, 5))
                    .endDate(LocalDate.of(2025, 10, 7))
                    .submissionDeadline(LocalDate.of(2025, 8, 1))
                    .maxParticipants(200)
                    .topic("Cybersecurity & Privacy")
                    .status(Conference.ConferenceStatus.UPCOMING)
                    .build());

            confRepo.save(Conference.builder()
                    .title("Global Conference on Data Science 2024")
                    .description("Exploring the latest advances in data analytics, big data pipelines, and ML applications.")
                    .venue("Virtual (Online)")
                    .startDate(LocalDate.of(2024, 11, 20))
                    .endDate(LocalDate.of(2024, 11, 22))
                    .submissionDeadline(LocalDate.of(2024, 9, 30))
                    .maxParticipants(500)
                    .topic("Data Science & Analytics")
                    .status(Conference.ConferenceStatus.COMPLETED)
                    .build());

            sessionRepo.save(Session.builder()
                    .title("Opening Keynote: The Future of AI")
                    .startTime(LocalDateTime.of(2025, 9, 15, 9, 0))
                    .endTime(LocalDateTime.of(2025, 9, 15, 10, 0))
                    .speaker("Prof. Yoshua Bengio").room("Main Hall A")
                    .description("A visionary talk on the trajectory of AI research.")
                    .conference(aiConf).build());

            sessionRepo.save(Session.builder()
                    .title("Deep Learning in Healthcare Diagnostics")
                    .startTime(LocalDateTime.of(2025, 9, 15, 10, 30))
                    .endTime(LocalDateTime.of(2025, 9, 15, 12, 0))
                    .speaker("Dr. Fei-Fei Li").room("Hall B")
                    .description("Applications of deep neural networks in medical imaging.")
                    .conference(aiConf).build());

            sessionRepo.save(Session.builder()
                    .title("Zero-Trust Architecture in Modern Enterprises")
                    .startTime(LocalDateTime.of(2025, 10, 5, 9, 30))
                    .endTime(LocalDateTime.of(2025, 10, 5, 11, 0))
                    .speaker("Dr. John Kindervag").room("Security Lab 1")
                    .description("Practical zero-trust security models across hybrid infrastructure.")
                    .conference(secConf).build());

            log.info("✓ Sample conferences and sessions seeded.");
        }

        log.info("✓ DataInitializer complete.");
    }
}
