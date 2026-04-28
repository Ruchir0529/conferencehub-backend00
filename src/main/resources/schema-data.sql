-- ============================================================
-- Conference Hub — MySQL Database Schema & Sample Data
-- Run this script ONCE after creating the database.
-- The Spring Boot app will auto-create tables via JPA,
-- but this script adds sample data and useful indexes.
-- ============================================================

CREATE DATABASE IF NOT EXISTS conferencehub CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE conferencehub;

-- ── Sample Admin (password: Admin@123) ──────────────────────────────
INSERT IGNORE INTO users (name, email, password, role, institution, created_at, updated_at)
VALUES (
  'System Admin',
  'admin@conferencehub.com',
  '$2a$10$Nmu8jP.k6KCJKaJQUHbEi.MH9l5pVzUiN7S6GnS3dRzHwolJkNOFe',
  'ADMIN',
  'Conference Hub',
  NOW(), NOW()
);

-- ── Sample Participants (password for all: Test@1234) ────────────────
INSERT IGNORE INTO users (name, email, password, role, institution, created_at, updated_at) VALUES
('Dr. Alice Johnson',  'alice@university.edu',  '$2a$10$wjKH3qVvnMTiQGkbdM3jRuuiGchEWQQopSEwMMqiH91rjKVWb.6q2', 'PARTICIPANT', 'MIT',            NOW(), NOW()),
('Prof. Bob Williams', 'bob@research.org',      '$2a$10$wjKH3qVvnMTiQGkbdM3jRuuiGchEWQQopSEwMMqiH91rjKVWb.6q2', 'PARTICIPANT', 'Stanford',       NOW(), NOW()),
('Dr. Carol Martinez', 'carol@tech.edu',        '$2a$10$wjKH3qVvnMTiQGkbdM3jRuuiGchEWQQopSEwMMqiH91rjKVWb.6q2', 'PARTICIPANT', 'Carnegie Mellon', NOW(), NOW()),
('Mr. David Chen',     'david@postgrad.edu',    '$2a$10$wjKH3qVvnMTiQGkbdM3jRuuiGchEWQQopSEwMMqiH91rjKVWb.6q2', 'PARTICIPANT', 'UC Berkeley',    NOW(), NOW());

-- ── Sample Conferences ───────────────────────────────────────────────
INSERT IGNORE INTO conferences
  (title, description, venue, start_date, end_date, submission_deadline, max_participants, topic, status, created_at)
VALUES
(
  'International Conference on Artificial Intelligence 2025',
  'A premier forum for AI researchers and practitioners to present cutting-edge research and innovations.',
  'Hyderabad International Convention Centre, Hyderabad',
  '2025-09-15', '2025-09-18', '2025-07-01', 300,
  'Artificial Intelligence & Machine Learning', 'UPCOMING', NOW()
),
(
  'World Symposium on Cybersecurity 2025',
  'Bringing together security experts to address emerging threats and mitigation strategies.',
  'HITEX Exhibition Centre, Hyderabad',
  '2025-10-05', '2025-10-07', '2025-08-01', 200,
  'Cybersecurity & Privacy', 'UPCOMING', NOW()
),
(
  'Global Conference on Data Science 2024',
  'Exploring the latest advances in data analytics, big data, and machine learning applications.',
  'Virtual (Online)',
  '2024-11-20', '2024-11-22', '2024-09-30', 500,
  'Data Science & Analytics', 'COMPLETED', NOW()
);

-- ── Sample Sessions ──────────────────────────────────────────────────
INSERT IGNORE INTO sessions (title, start_time, end_time, speaker, room, description, conference_id)
SELECT
  'Opening Keynote: The Future of AI',
  CONCAT(start_date, ' 09:00:00'),
  CONCAT(start_date, ' 10:00:00'),
  'Prof. Yoshua Bengio',
  'Main Hall A',
  'A visionary talk on the trajectory of artificial intelligence research.',
  id
FROM conferences WHERE title LIKE '%Artificial Intelligence%';

INSERT IGNORE INTO sessions (title, start_time, end_time, speaker, room, description, conference_id)
SELECT
  'Deep Learning in Healthcare',
  CONCAT(start_date, ' 10:30:00'),
  CONCAT(start_date, ' 12:00:00'),
  'Dr. Fei-Fei Li',
  'Hall B',
  'Applications of deep neural networks in medical diagnosis and treatment planning.',
  id
FROM conferences WHERE title LIKE '%Artificial Intelligence%';

INSERT IGNORE INTO sessions (title, start_time, end_time, speaker, room, description, conference_id)
SELECT
  'Zero-Trust Architecture in Modern Enterprises',
  CONCAT(start_date, ' 09:30:00'),
  CONCAT(start_date, ' 11:00:00'),
  'Dr. John Kindervag',
  'Security Lab 1',
  'Implementing zero-trust security models across distributed infrastructure.',
  id
FROM conferences WHERE title LIKE '%Cybersecurity%';

-- ── Useful Indexes ───────────────────────────────────────────────────
CREATE INDEX IF NOT EXISTS idx_registrations_conference ON registrations(conference_id);
CREATE INDEX IF NOT EXISTS idx_registrations_user       ON registrations(user_id);
CREATE INDEX IF NOT EXISTS idx_papers_conference        ON paper_submissions(conference_id);
CREATE INDEX IF NOT EXISTS idx_papers_user              ON paper_submissions(submitted_by);
CREATE INDEX IF NOT EXISTS idx_reviews_paper            ON reviews(paper_id);
CREATE INDEX IF NOT EXISTS idx_reviews_reviewer         ON reviews(reviewer_id);
