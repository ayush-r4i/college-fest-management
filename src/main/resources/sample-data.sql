-- ============================================================
-- College Fest & Hackathon Management System - Sample Data
-- ============================================================
-- OPTIONAL demo records so the UI isn't empty when you first explore it.
-- This file is NOT run automatically by Spring Boot (see README) - run it
-- yourself once, e.g.:
--   mysql -u root -p college_fest_db < sample-data.sql
-- Running it more than once will create duplicate events/teams/participants
-- since (unlike admin/judge) they don't have unique-name constraints.
-- ============================================================

-- ---------------- Events ----------------
INSERT INTO event (name, description, venue, category, start_time, end_time, max_team_size, created_at) VALUES
('CodeStorm Hackathon', '24-hour coding hackathon open to all branches', 'Main Auditorium', 'Hackathon', '2026-08-10 09:00:00', '2026-08-11 09:00:00', 4, NOW()),
('Robo Race', 'Line-following and obstacle robot race', 'Engineering Block Ground', 'Robotics', '2026-08-10 10:00:00', '2026-08-10 14:00:00', 3, NOW()),
('Cultural Night', 'Music, dance and drama showcase', 'Main Auditorium', 'Cultural', '2026-08-11 18:00:00', '2026-08-11 22:00:00', 10, NOW());

-- ---------------- Judges ----------------
INSERT IGNORE INTO judge (name, email, phone, expertise) VALUES
('Dr. Anjali Deshmukh', 'anjali.deshmukh@example.edu', '9876543210', 'AI/ML'),
('Prof. Suresh Kumar', 'suresh.kumar@example.edu', '9876500000', 'Software Engineering');

-- ---------------- Teams (linked to CodeStorm Hackathon / Robo Race) ----------------
INSERT INTO team (team_name, event_id, leader_name, leader_email, leader_phone, registered_at)
SELECT 'Byte Busters', id, 'Rohan Mehta', 'rohan.mehta@example.edu', '9998887771', NOW()
FROM event WHERE name = 'CodeStorm Hackathon' LIMIT 1;

INSERT INTO team (team_name, event_id, leader_name, leader_email, leader_phone, registered_at)
SELECT 'Code Ninjas', id, 'Priya Sharma', 'priya.sharma@example.edu', '9998887772', NOW()
FROM event WHERE name = 'CodeStorm Hackathon' LIMIT 1;

INSERT INTO team (team_name, event_id, leader_name, leader_email, leader_phone, registered_at)
SELECT 'Turbo Bots', id, 'Aman Verma', 'aman.verma@example.edu', '9998887773', NOW()
FROM event WHERE name = 'Robo Race' LIMIT 1;

-- ---------------- Participants ----------------
INSERT INTO participant (name, email, phone, college, team_id)
SELECT 'Rohan Mehta', 'rohan.mehta@example.edu', '9998887771', 'ABC College of Engineering', id
FROM team WHERE team_name = 'Byte Busters' LIMIT 1;

INSERT INTO participant (name, email, phone, college, team_id)
SELECT 'Aditi Rao', 'aditi.rao@example.edu', '9998887701', 'ABC College of Engineering', id
FROM team WHERE team_name = 'Byte Busters' LIMIT 1;

INSERT INTO participant (name, email, phone, college, team_id)
SELECT 'Priya Sharma', 'priya.sharma@example.edu', '9998887772', 'ABC College of Engineering', id
FROM team WHERE team_name = 'Code Ninjas' LIMIT 1;

INSERT INTO participant (name, email, phone, college, team_id)
SELECT 'Karan Singh', 'karan.singh@example.edu', '9998887702', 'ABC College of Engineering', id
FROM team WHERE team_name = 'Code Ninjas' LIMIT 1;

INSERT INTO participant (name, email, phone, college, team_id)
SELECT 'Aman Verma', 'aman.verma@example.edu', '9998887773', 'ABC College of Engineering', id
FROM team WHERE team_name = 'Turbo Bots' LIMIT 1;

INSERT INTO participant (name, email, phone, college, team_id)
SELECT 'Sneha Iyer', 'sneha.iyer@example.edu', '9998887703', 'ABC College of Engineering', id
FROM team WHERE team_name = 'Turbo Bots' LIMIT 1;

-- ---------------- Scores (final_score = average of the 5 criteria) ----------------
INSERT INTO score (team_id, judge_id, innovation, technical_implementation, presentation, ui_ux, overall_impression, final_score, scored_at)
SELECT t.id, j.id, 8.5, 8.0, 7.5, 8.0, 8.0, 8.0, NOW()
FROM team t, judge j
WHERE t.team_name = 'Byte Busters' AND j.email = 'anjali.deshmukh@example.edu'
LIMIT 1;

INSERT INTO score (team_id, judge_id, innovation, technical_implementation, presentation, ui_ux, overall_impression, final_score, scored_at)
SELECT t.id, j.id, 9.0, 8.5, 8.0, 8.5, 9.0, 8.6, NOW()
FROM team t, judge j
WHERE t.team_name = 'Byte Busters' AND j.email = 'suresh.kumar@example.edu'
LIMIT 1;

INSERT INTO score (team_id, judge_id, innovation, technical_implementation, presentation, ui_ux, overall_impression, final_score, scored_at)
SELECT t.id, j.id, 7.0, 7.5, 8.0, 7.0, 7.5, 7.4, NOW()
FROM team t, judge j
WHERE t.team_name = 'Code Ninjas' AND j.email = 'anjali.deshmukh@example.edu'
LIMIT 1;
