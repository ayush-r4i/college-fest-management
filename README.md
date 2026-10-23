# College Fest & Hackathon Management System

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Database](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-darkgreen.svg)](https://www.thymeleaf.org/)
[![License](https://img.shields.io/badge/License-Academic-lightgrey.svg)]()

A robust, full-stack Spring Boot management platform designed for college festivals, competitions, and hackathons. It orchestrates event scheduling with venue conflict detection, team registration with automated leader onboarding, participant quota enforcement, panel judge scoring across multi-dimensional criteria, and an auto-refreshing live leaderboard with event filtering.

The application features a **Linear-inspired minimalist SaaS dashboard** built with clean HTML5, custom vanilla CSS, and server-rendered Thymeleaf—free of heavy frontend framework overhead.

---

## Table of Contents

1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Architecture & Directory Structure](#architecture--directory-structure)
4. [Prerequisites](#prerequisites)
5. [Database Configuration](#database-configuration)
6. [Running the Application](#running-the-application)
7. [REST API & Postman](#rest-api--postman)
8. [Business Rules & Validation](#business-rules--validation)

---

## Features

### 📅 Event Scheduling & Conflict Prevention
- Complete CRUD operations for competitions, cultural events, and hackathons.
- Automated **venue-time conflict detection**: prevents overlapping events at the same venue.
- Per-event rules configuring competition time windows and maximum team capacity limits.

### 👥 Streamlined Team Registration
- **Automated Leader Enrollment**: Registering a team automatically generates a `Participant` record for the team leader, designating them as **Participant #1**.
- **Seamless Roster Building**: Successful team creation immediately redirects to the Add Member page with the newly created team pre-selected.
- **Duplicate Protection**: Prevents duplicate team names within the same event and blocks re-adding the leader or duplicate members to the roster.

### 🎓 Participant Management
- Enforces event-specific capacity ceilings (`maxTeamSize`).
- Tracks participant records, contact details, and institutional affiliations.
- Team-locked participant assignments to maintain data integrity.

### ⚖️ Panel Judge Directory & Evaluation
- Directory of judges with categorized domain expertise.
- **5-Dimensional Scoring Model** (0.0 to 10.0 scale):
  - Innovation
  - Technical Execution
  - Presentation & Pitch
  - UI / UX Design
  - Overall Impression
- **Automated Weighted Averaging**: Automatically calculates final scores rounded to two decimal places.
- **Idempotent Upsert**: Re-scoring an existing team-judge pair updates the score without creating duplicate records.

### 🏆 Live Event-Specific Leaderboard
- **Dynamic Event Filter**: Dropdown dynamically populated with available events to isolate standings per competition.
- **Real-Time Standings**: Auto-refreshes every 15 seconds (`Live Sync`) via meta-refresh when viewing an active competition.
- **Podium Visuals**: Minimalist badge indicators for Rank 1 (Gold), Rank 2 (Silver), and Rank 3 (Bronze).
- **Graceful Empty States**: Informative prompt cards when no event is selected or when an event has no submitted scores yet.

### 🎨 Linear-Inspired SaaS Interface
- Modern, clean aesthetic utilizing an indigo primary palette (`#4F46E5`), crisp card surfaces, dark slate typography (`#0F172A`), and subtle borders.
- Dark minimalist navigation bar with lightweight SVG icons (no emoji clutter).
- Responsive data tables with horizontal scroll wrappers for mobile, tablet, and desktop viewports.

---

## Tech Stack

| Layer | Technologies |
| :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.5.4 (Spring Web MVC, Spring Data JPA, Hibernate, Bean Validation) |
| **Frontend** | Thymeleaf 3, HTML5, Custom Vanilla CSS (Design Tokens, Responsive Grid) |
| **Database** | MySQL 8.0+ with HikariCP connection pooling |
| **Security** | Lightweight `HttpSession` authentication with `SessionAuthInterceptor` |
| **Build & Tooling** | Apache Maven 3.9+, IntelliJ IDEA |
| **Testing** | JUnit 5, Mockito, Spring Boot Starter Test |

---

## Architecture & Directory Structure

```
college-fest-management/
├── pom.xml                                      # Maven build dependencies & plugins
├── README.md                                    # Project documentation
├── postman/
│   └── College-Fest-Management.postman_collection.json # Ready-to-import Postman suite
└── src/
    ├── main/
    │   ├── java/com/collegefest/management/
    │   │   ├── CollegeFestManagementApplication.java  # Main entry point
    │   │   ├── api/                             # REST controllers serving JSON
    │   │   ├── config/                          # Interceptor & WebMvc configuration
    │   │   ├── dto/                             # Data Transfer Objects (LeaderboardEntry)
    │   │   ├── entity/                          # JPA Entities (Admin, Event, Team, etc.)
    │   │   ├── exception/                       # Custom business exceptions & GlobalExceptionHandler
    │   │   ├── repository/                      # Spring Data JPA Repository interfaces
    │   │   ├── service/                         # Business logic, capacity & conflict checks
    │   │   └── web/                             # Spring MVC Controllers serving Thymeleaf views
    │   └── resources/
    │       ├── application.properties           # Database & server configuration
    │       ├── schema.sql                       # Database initialization & default admin
    │       ├── sample-data.sql                  # Optional demo dataset
    │       ├── static/css/style.css             # Linear-inspired design system
    │       └── templates/                       # Thymeleaf templates & fragments
    └── test/
        └── java/com/collegefest/management/     # Unit and integration test suites
```

---

## Prerequisites

Before running the application, ensure you have:
- **Java Development Kit (JDK) 21** or higher (`java -version`)
- **Apache Maven 3.9+** (optional if using IntelliJ's bundled Maven)
- **MySQL Server 8.0+** running locally on port `3306`
- **IntelliJ IDEA** (Community or Ultimate) or any preferred Java IDE

---

## Database Configuration

### 1. Create the Database
Log into your MySQL instance (via terminal, MySQL Workbench, or Command Line Client) and create the database:

```sql
CREATE DATABASE college_fest_db;
```

> **Note:** You do not need to create tables manually. Hibernate creates all tables automatically on startup (`spring.jpa.hibernate.ddl-auto=update`), and `schema.sql` creates the default admin user.

### 2. Configure Credentials
Open `src/main/resources/application.properties` and verify your local MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/college_fest_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=ayush
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

*(Update `username` and `password` to match your local MySQL configuration if different).*

---

## Running the Application

### Option A: From IntelliJ IDEA (Recommended)
1. Open IntelliJ IDEA &rarr; **File** &rarr; **Open** &rarr; select the `college-fest-management` folder.
2. Allow Maven to index and import dependencies.
3. Ensure the project SDK is set to Java 21 (**File** &rarr; **Project Structure** &rarr; **Project** &rarr; **SDK: 21**).
4. Navigate to `src/main/java/com/collegefest/management/CollegeFestManagementApplication.java`.
5. Click the green **Run** arrow next to the `main` method (or press `Shift + F10`).

### Option B: From the Terminal
```bash
# Navigate to the project root
cd college-fest-management

# Run via the Spring Boot Maven Plugin
mvn spring-boot:run
```

Once started, the embedded Tomcat container will initialize on port `8080`.

### Accessing the Web Portal
- Open your browser at: **`http://localhost:8080`**
- You will be redirected to the login page (`/login`).
- **Default Admin Credentials**:
  - **Username**: `admin`
  - **Password**: `admin123`

### (Optional) Load Demo Sample Data
To populate sample events, teams, judges, participants, and scores for demonstration purposes:

```bash
mysql -u root -p college_fest_db < src/main/resources/sample-data.sql
```

---

## REST API & Postman

In addition to the web UI, the application exposes a RESTful JSON API (`/api/**`) for automated testing and client integration.

### Importing Postman Collection
1. Open Postman &rarr; Click **Import**.
2. Select `postman/College-Fest-Management.postman_collection.json`.
3. The collection is configured with default `{{baseUrl}} = http://localhost:8080`.

### Key Endpoints

| Resource | HTTP Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **Events** | `GET` | `/api/events` | List all events |
| | `POST` | `/api/events` | Create a new event (validates venue conflicts) |
| **Teams** | `GET` | `/api/teams` | List all teams |
| | `POST` | `/api/teams?eventId={id}` | Register a new team under an event |
| **Participants** | `GET` | `/api/participants` | List all participants |
| | `POST` | `/api/participants?teamId={id}` | Add a participant to a team |
| **Judges** | `GET` | `/api/judges` | List all judges |
| | `POST` | `/api/judges` | Register a new judge |
| **Scores** | `POST` | `/api/scores?teamId={id}&judgeId={id}` | Submit / update evaluation scores |
| **Leaderboard** | `GET` | `/api/leaderboard` | Get composite global leaderboard |
| | `GET` | `/api/leaderboard?eventId={id}` | Get event-specific ranked leaderboard |

All API error responses follow standard HTTP semantics (e.g., `400 Bad Request`, `404 Not Found`, `409 Conflict`) handled by `GlobalExceptionHandler`.

---

## Business Rules & Validation

1. **Venue Conflict Prevention**: An event cannot be scheduled in a venue if another event overlaps with the same venue during that time window.
2. **Team Capacity Limits**: Teams cannot exceed the maximum participant count defined by the event (`maxTeamSize`).
3. **Automated Team Leader Registration**: Team leaders are registered as Participant #1 upon team creation, guaranteeing accurate roster tracking.
4. **Duplicate Prevention**: Teams within the same event cannot share identical names. Participants cannot be registered twice on the same team.
5. **Score Validation**: Each criterion must be within the range `0.0` to `10.0`. The final composite score is calculated as the unweighted mean of all five dimensions.
