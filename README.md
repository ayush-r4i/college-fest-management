# College Fest & Hackathon Management System

A beginner-friendly Spring Boot minor project for managing a college fest / hackathon:
events, team registration, participants, judges, scoring, and a live leaderboard.

**Stack:** Java 21 • Spring Boot 3.5.4 • Maven • MySQL • Spring Data JPA • Thymeleaf • HTML/CSS

**Intentionally NOT used:** React, Spring Security, JWT, Docker, Lombok, microservices, Redis, Kafka.

---

## 1. What's inside

```
college-fest-management/
├── pom.xml
├── README.md
├── postman/
│   └── College-Fest-Management.postman_collection.json
└── src/main/
    ├── java/com/collegefest/management/
    │   ├── CollegeFestManagementApplication.java
    │   ├── entity/        Admin, Event, Team, Participant, Judge, Score
    │   ├── repository/    Spring Data JPA repositories
    │   ├── dto/            LeaderboardEntry (read-only leaderboard row)
    │   ├── exception/      Custom exceptions + GlobalExceptionHandler
    │   ├── service/        Business logic (conflict checks, capacity checks, scoring)
    │   ├── web/             Thymeleaf (browser) controllers, session-based login
    │   ├── api/              REST (JSON) controllers, for Postman
    │   └── config/         WebConfig + SessionAuthInterceptor (login protection)
    └── resources/
        ├── application.properties
        ├── schema.sql        (auto-runs on every startup, safe to re-run)
        ├── sample-data.sql   (optional demo data, run manually - see step 6)
        ├── static/css/style.css
        └── templates/        Thymeleaf pages (login, dashboard, events, teams, ...)
```

### How the pieces fit together
- **Web pages** (`/dashboard`, `/events`, `/teams`, ...) are server-rendered with Thymeleaf and
  protected by a simple `HttpSession` check (`SessionAuthInterceptor`) - no Spring Security.
- **REST API** (`/api/events`, `/api/teams`, ...) returns JSON and is meant for testing with Postman.
  It is left open (no login required) to keep things simple for a learning project.
- **Business rules** live in the `service` layer:
  - `EventService` rejects a new/updated event if another event is already booked at the
    same venue during an overlapping time window (`VenueConflictException`).
  - `TeamService` / `ParticipantService` reject adding a member once a team has reached the
    event's `maxTeamSize` (`TeamCapacityExceededException`).
  - `ScoreService` validates each criterion is between 0-10, automatically calculates
    `finalScore` as the average of the five criteria, and builds the ranked leaderboard.

---

## 2. Prerequisites

- **JDK 21** installed (`java -version` should show 21.x)
- **Maven 3.9.x** (IntelliJ ships with a bundled Maven, so a separate install is optional)
- **MySQL Community Server** installed and running
- **IntelliJ IDEA Community Edition**
- **Postman** (for testing the REST API)

---

## 3. Step 1 - Create the MySQL database

Open a terminal (or MySQL Workbench / MySQL Shell) and run:

```sql
CREATE DATABASE college_fest_db;
```

That's it - you do **not** need to manually create tables. When the app starts:
1. Hibernate creates all tables automatically (`spring.jpa.hibernate.ddl-auto=update`).
2. `schema.sql` then runs too (using `CREATE TABLE IF NOT EXISTS`, so it's safe and won't
   conflict with what Hibernate just created) and inserts a default admin login.

If you'd rather create the database with a GUI, MySQL Workbench → right-click "Schemas" →
"Create Schema" → name it `college_fest_db` works the same way.

---

## 4. Step 2 - Configure the database connection

Open `src/main/resources/application.properties` and update these three lines to match
your local MySQL setup:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/college_fest_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
```

- `username` / `password` → whatever you set up for your local MySQL root user (or a
  dedicated user you created for this project).
- If MySQL is running on a different port, change `3306` accordingly.
- If you use MySQL 5.7 instead of 8+, you can leave `useSSL=false` as is.

---

## 5. Step 3 - Import the project into IntelliJ

1. Unzip the downloaded project anywhere on your machine.
2. Open IntelliJ IDEA → **File → Open** → select the `college-fest-management` folder
   (the one containing `pom.xml`).
3. IntelliJ will detect it's a Maven project and show a small popup/notification bar - click
   **"Load Maven Project"** (or **"Import Changes"**) if prompted.
4. Wait for the status bar (bottom-right) to finish "Downloading" / "Indexing" - this is Maven
   pulling all the dependencies listed in `pom.xml` for the first time.
5. Make sure the Project SDK is set to Java 21:
   **File → Project Structure → Project → SDK → 21**.

---

## 6. Step 4 - Run the application

**Option A - from IntelliJ:**
Open `CollegeFestManagementApplication.java` (in `src/main/java/com/collegefest/management/`)
and click the green ▶ Run arrow next to the `main` method, or the class name.

**Option B - from a terminal, using Maven:**
```bash
cd college-fest-management
mvn spring-boot:run
```

You should see Spring Boot's startup banner, followed by a line like:
```
Tomcat started on port 8080 (http) ...
Started CollegeFestManagementApplication in X.XXX seconds
```

Now open a browser to **http://localhost:8080** - it will redirect you to the login page.

**Default login:** `admin` / `admin123` (created automatically by `schema.sql`).

### Optional: load demo sample data
`sample-data.sql` is **not** run automatically (so restarting the app never duplicates
demo rows). To load some example events/teams/participants/judges/scores once:

```bash
mysql -u root -p college_fest_db < src/main/resources/sample-data.sql
```

(Run this while the app is stopped or running - either is fine.)

---

## 7. Step 5 - Test the APIs using Postman

1. Open Postman → **Import** → select
   `postman/College-Fest-Management.postman_collection.json`.
2. The collection uses a `{{baseUrl}}` variable already set to `http://localhost:8080` -
   change it if your app runs on a different port.
3. It also has `{{eventId}}`, `{{teamId}}`, `{{judgeId}}`, etc. variables (default `1`) -
   update these to match the actual IDs returned by your "Create"/"Get All" requests.
4. Suggested order to try things out:
   1. **Events → Create Event** (note the returned `id`)
   2. **Events → Create Event (Venue Conflict Example)** → should return **409 Conflict**
      because it overlaps the first event's venue/time
   3. **Judges → Create Judge**
   4. **Teams → Register Team** (pass `?eventId=<id from step 1>`)
   5. **Participants → Add Participant** (pass `?teamId=<id from step 4>`) - try adding more
      participants than the event's `maxTeamSize` to see the **400 Bad Request** capacity error
   6. **Scores → Submit Score** (pass `?teamId=...&judgeId=...`) - `finalScore` in the response
      is calculated automatically
   7. **Leaderboard → Get Leaderboard** - see teams ranked by their average final score

All error responses (404, 409, 400, etc.) come from `GlobalExceptionHandler` and look like:
```json
{
  "timestamp": "2026-07-25T10:15:30",
  "status": 409,
  "error": "Conflict",
  "message": "Venue conflict: \"CodeStorm Hackathon\" is already scheduled at Main Auditorium ...",
  "path": "/api/events"
}
```

---

## 8. Common errors and how to fix them

| Error | Likely Cause | Fix |
|---|---|---|
| `Communications link failure` / `Connection refused` | MySQL isn't running, or wrong port | Start the MySQL service; verify the port in `application.properties` |
| `Access denied for user 'root'@'localhost'` | Wrong username/password | Update `spring.datasource.username` / `password` in `application.properties` |
| `Unknown database 'college_fest_db'` | You skipped Step 1 | Run `CREATE DATABASE college_fest_db;` in MySQL |
| `Public Key Retrieval is not allowed` | MySQL 8 default auth plugin | Make sure the URL includes `allowPublicKeyRetrieval=true` (already set) |
| `Port 8080 was already in use` | Another app (or a previous run) is using port 8080 | Stop the other process, or add `server.port=8081` to `application.properties` |
| `Table 'xxx' doesn't exist` right after first run | Hibernate/schema.sql didn't get a chance to run | Stop the app, confirm the datasource URL/db name, restart |
| `Duplicate entry ... for key 'username'` when re-running scripts | You ran an INSERT script (not schema.sql) more than once | `schema.sql`'s admin insert uses `INSERT IGNORE`, so it's safe; if you manually re-ran `sample-data.sql`, either ignore the duplicates or clear the tables first |
| Login page keeps reappearing after entering `admin`/`admin123` | Typo, or `schema.sql` never created the admin row | Check the console log for SQL errors on startup; confirm the `admin` table has a row via MySQL Workbench |
| `Whitelabel Error Page` | An unhandled exception in a web controller | Check the IntelliJ "Run" console for the stack trace - the message usually points to the exact cause |
| Maven can't download dependencies | No internet access, or a proxy/firewall is blocking Maven Central | Check your network connection; corporate networks sometimes need proxy settings in `~/.m2/settings.xml` |
| `mvn: command not found` in terminal | Maven isn't on your system PATH | Just run the app from IntelliJ instead (Option A in Step 4), which uses IntelliJ's bundled Maven |

---

## 9. Debugging checklist

If something isn't working, check things in this order:

1. **Is MySQL actually running?** (`mysql -u root -p` should connect from a terminal)
2. **Does `college_fest_db` exist?** (`SHOW DATABASES;` inside the MySQL client)
3. **Do the credentials in `application.properties` match what you use to log into MySQL manually?**
4. **What does the IntelliJ "Run" console say?** Scroll up from the bottom - the *first*
   exception in a long stack trace is usually the real cause; everything after it is often
   just Spring wrapping/re-throwing.
5. **Are you hitting the right URL?** Web pages are unprefixed (`/dashboard`, `/events`),
   while the JSON API is under `/api/...` (`/api/events`).
6. **Did you forget to log in?** Any web page other than `/login` will redirect you back to
   `/login` if there's no active session.

If you run into an error you can't resolve, copy the **full stack trace** from the console
(not just the last line) - the exact exception type and message make it much faster to pin
down what went wrong.

---

## 10. Feature summary

- ✅ Admin login via `HttpSession` (no Spring Security)
- ✅ Event CRUD with automatic venue/time conflict detection
- ✅ Team registration with a per-event member limit
- ✅ Participant management, enforcing the team's member limit
- ✅ Judge CRUD
- ✅ Score submission (Innovation, Technical Implementation, Presentation, UI/UX,
  Overall Impression) with an automatically calculated final score
- ✅ Live leaderboard, auto-ranked by average final score (auto-refreshes every 15s)
- ✅ Clean Thymeleaf UI with a shared navigation bar
- ✅ REST API + Postman collection for direct backend testing
