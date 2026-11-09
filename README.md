# WASI – MATH WHIZ

A web-based math game built with Java Servlet, JSP and JDBC. Players answer four math questions (prime, modulus, two trigonometry). Every answer is checked by real Java methods, and scores are saved in MySQL and shown on a Top 10 leaderboard.

## Project Objectives
- Build reusable Java methods for prime, modulus and trigonometry
- Handle requests and responses with a Servlet and JSP pages
- Connect to MySQL using JDBC and `PreparedStatement`
- Store, sort and display scores (Top 10 leaderboard)
- Apply per-question difficulty marking: **mark = question mark × difficulty multiplier**

## Technologies Used
- Java 17
- Jakarta Servlet 6 / JSP / JSTL
- JDBC, MySQL 8.0
- Maven
- Apache Tomcat 11
- IntelliJ IDEA

## Software Requirements
- JDK 17 or newer
- Apache Tomcat 11
- MySQL Server 8.0
- Maven 3.6+ (bundled with IntelliJ)
- IntelliJ IDEA (any Java IDE works)

## Project Structure
```
MathWhiz/
├── src/main/java/
│   ├── controller/GameServlet.java
│   ├── model/ (Difficulty, Question, QuestionResult, GameScore)
│   ├── service/ (MathService, QuestionFactory)
│   └── dao/ScoreDAO.java
├── src/main/webapp/
│   ├── index.jsp, game.jsp, result.jsp, leaderboard.jsp
│   └── css/style.css
├── src/test/java/service/MathServiceTest.java
├── database/math_whiz_db.sql
├── screenshots/
└── README.md
```

## Game Rules and Scoring System

| Question | Area | Base Mark |
|---|---|---|
| Q1 | Prime Number (1 = prime, 0 = not prime) | 2 |
| Q2 | Modulus (integers only, divisor ≠ 0) | 2 |
| Q3 | Trigonometry (sin / cos / tan, degrees) | 3 |
| Q4 | Trigonometry (sin / cos / tan, degrees) | 3 |

Each question gets its own random difficulty:

| Difficulty | Multiplier |
|---|---|
| EASY | ×1 |
| NORMAL | ×2 |
| HARD | ×3 |

**Mark of a question = question mark × difficulty multiplier.**
Example: Q3 (3 marks) on HARD = 3 × 3 = 9 marks. The maximum score changes per game (10 to 30) and is saved as `max_score`.

Rules:
- Numbers less than 2 are not prime.
- Modulus divisor must not be zero (validation error).
- Trigonometry takes degrees and converts to radians before using `Math.sin()`, `Math.cos()`, `Math.tan()`.
- Decimal answers are compared with a tolerance of 0.01 (round to 2 decimals).
- Tangent at 90° and 270° is undefined: type `undefined`.
- Harder questions use larger numbers, negative operands and more angles.

## Database Configuration
Default settings in `src/main/java/dao/ScoreDAO.java`:

| Setting | Default |
|---|---|
| URL | `jdbc:mysql://localhost:3306/math_whiz_db` |
| User | `root` |
| Password | set `PASS` in `ScoreDAO.java` to your MySQL root password |

You can also use environment variables: `MW_DB_URL`, `MW_DB_USER`, `MW_DB_PASS`.

## Database Creation SQL
```sql
CREATE DATABASE IF NOT EXISTS math_whiz_db;
USE math_whiz_db;

CREATE TABLE IF NOT EXISTS game_scores (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    player_name VARCHAR(100) NOT NULL,
    score       INT NOT NULL,
    max_score   INT NOT NULL,
    difficulty  VARCHAR(50) NOT NULL,
    played_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

Leaderboard query:
```sql
SELECT * FROM game_scores ORDER BY score DESC, played_at ASC LIMIT 10;
```

## Steps to Run the Application
1. Clone or download this repository.
2. Run `database/math_whiz_db.sql` in MySQL.
3. Open the `MathWhiz` folder in IntelliJ IDEA as a Maven project and wait for dependencies to download.
4. Set the project JDK to 17 or newer.
5. Set your MySQL password in `ScoreDAO.java`.
6. Add a **Tomcat 11 Local** run configuration with artifact `MathWhiz:war exploded` and application context `/MathWhiz`.
7. Run it and open `http://localhost:8080/MathWhiz/`.

Alternative: run `mvn clean package` and copy `target/MathWhiz.war` into Tomcat's `webapps` folder.

## Testing
`MathServiceTest` checks the math methods and the marking rule.

| Test Case | Expected Result |
|---|---|
| Prime 7 | 1 |
| Prime 8 | 0 |
| Prime 1 | 0 |
| 17 % 5 | 2 |
| sin 30° | 0.5 |
| cos 60° | 0.5 |
| tan 45° | 1.0 |
| Divisor 0 | Validation error |
| All answers correct | Maximum marks for the game |
| Wrong answers | Marks for those questions not awarded |

## Application Screenshots

### Welcome Page
![Welcome Page]
<img width="1197" height="623" alt="Screenshot 2026-11-09 162525" src="https://github.com/user-attachments/assets/7285fb95-6416-43f7-95f6-29c2f0822999" />


### Game Page
![Game Page]
<img width="1146" height="967" alt="Screenshot 2026-11-09 162559" src="https://github.com/user-attachments/assets/757893f5-90ca-4643-980d-835d10b0dbac" />


### Result Page
![Result Page]
<img width="956" height="562" alt="Screenshot 2026-11-09 162728" src="https://github.com/user-attachments/assets/e13902e3-5f9d-4a4f-8739-5dd9464d76f3" />

### Leaderboard
![Leaderboard]

<img width="1483" height="687" alt="Screenshot 2026-11-09 162515" src="https://github.com/user-attachments/assets/0829fa30-7ed0-4dfd-8760-b9df083cb6de" />

## GitHub Project Directory
https://github.com/username/repository/tree/main/MathWhiz
