# WASI – MATH WHIZ

A web-based math game built with **Java Servlet, JSP and JDBC**. Players enter a name, answer four math questions (prime, modulus, two trigonometry), and see their score. Every answer is checked by real Java methods (nothing is stored as a fixed answer), and each game is saved in MySQL and ranked on a **Top 10 leaderboard**.

Special feature: **every question has its own difficulty**, and the marks change with it: **mark = question mark x difficulty multiplier**.

---

## Table of Contents
1. [Project Objectives](#project-objectives)
2. [Technologies Used](#technologies-used)
3. [Software Requirements](#software-requirements)
4. [Project Structure](#project-structure)
5. [How the Game Works](#how-the-game-works)
6. [Game Rules and Scoring System](#game-rules-and-scoring-system)
7. [Database Configuration](#database-configuration)
8. [Database Creation SQL](#database-creation-sql)
9. [Steps to Run the Application](#steps-to-run-the-application)
10. [Source Code](#source-code)
11. [Testing and Validation](#testing-and-validation)
12. [Troubleshooting](#troubleshooting)
13. [Application Screenshots](#application-screenshots)
14. [GitHub Project Directory](#github-project-directory)

---

## Project Objectives
- Build reusable Java methods for prime check, modulus and trigonometry
- Use a Servlet and JSP pages to handle requests, sessions and responses
- Connect to MySQL with JDBC and `PreparedStatement`
- Save player name, score, difficulty and date/time
- Retrieve, sort and display the Top 10 scores
- Apply per-question difficulty marking (mark = question mark x difficulty multiplier)
- Document and submit the project on GitHub

## Technologies Used
| Layer | Technology |
|---|---|
| Language | Java 17 |
| Web | Jakarta Servlet 6, JSP, JSTL |
| Database | MySQL 8.0, JDBC (`PreparedStatement`) |
| Build | Maven (WAR packaging) |
| Server | Apache Tomcat 11 |
| IDE | IntelliJ IDEA |

## Software Requirements
- JDK 17 or newer
- Apache Tomcat 11
- MySQL Server 8.0
- Maven 3.6+ (bundled with IntelliJ)
- IntelliJ IDEA (any Java IDE works)

## Project Structure
```
MathWhiz/
├── pom.xml
├── src/main/java/
│   ├── controller/GameServlet.java      # /start, /submit, /leaderboard
│   ├── model/
│   │   ├── Difficulty.java              # EASY x1, NORMAL x2, HARD x3
│   │   ├── Question.java                # one question + its marks + checking
│   │   ├── QuestionResult.java          # player's answer for one question
│   │   └── GameScore.java               # one row of game_scores
│   ├── service/
│   │   ├── MathService.java             # isPrime, calculateMod, sin/cos/tan
│   │   └── QuestionFactory.java         # generates 4 random questions
│   └── dao/ScoreDAO.java                # JDBC insert + top 10 select
├── src/main/webapp/
│   ├── index.jsp                        # Screen 1: Welcome
│   ├── game.jsp                         # Screen 2: Game
│   ├── result.jsp                       # Screen 3: Result
│   ├── leaderboard.jsp                  # Screen 4: Leaderboard
│   └── css/style.css
├── src/test/java/service/MathServiceTest.java
├── database/math_whiz_db.sql
├── screenshots/
└── README.md
```

## How the Game Works
1. **Welcome page (`index.jsp`)**: player types a name and clicks *Start Game* (POST `/start`).
2. **`GameServlet.start`** validates the name, asks `QuestionFactory` for four questions (each with a random difficulty), keeps them in the session, and forwards to `game.jsp`.
3. **Game page (`game.jsp`)**: shows Q1 to Q4 with difficulty tag and marks (for example `3 x 3 = 9 marks`). Player fills the answers and clicks *Submit Answers* (POST `/submit`).
4. **`GameServlet.submit`** reads the answers, checks each one through `Question.isCorrect()` (which calls the `MathService` methods), adds up the marks, and saves the game with `ScoreDAO.save()`.
5. **Result page (`result.jsp`)** shows the player name, final score, correct answers and marks per question.
6. **Leaderboard (`leaderboard.jsp`)**: GET `/leaderboard` runs `ScoreDAO.top10()` and shows the best 10 games.

## Game Rules and Scoring System

| Question | Area | Base Mark |
|---|---|---|
| Q1 | Prime Number (1 = prime, 0 = not prime) | 2 |
| Q2 | Modulus (integers only, divisor not zero) | 2 |
| Q3 | Trigonometry (sin / cos / tan, degrees) | 3 |
| Q4 | Trigonometry (sin / cos / tan, degrees) | 3 |

### Difficulty multipliers (each question gets its own)
| Difficulty | Multiplier | Prime range | Modulus | Trig angles |
|---|---|---|---|---|
| EASY | x1 | 2 to 30 | small positive numbers | 0, 30, 60, 90 (sin / cos only) |
| NORMAL | x2 | 30 to 200 | bigger positive numbers | 0, 30, 45, 60, 90, 120, 135, 150, 180 (sin / cos / tan) |
| HARD | x3 | 200 to 2000 | can be negative | any multiple of 15 up to 360 (sin / cos / tan) |

### Marking formula
```
mark of a question = base mark x difficulty multiplier
total score        = sum of marks of the correctly answered questions
max score          = sum of marks of all four questions (changes per game)
```

### Example
| Question | Base | Difficulty | Marks if correct |
|---|---|---|---|
| Q1 Prime | 2 | HARD (x3) | 2 x 3 = 6 |
| Q2 Modulus | 2 | EASY (x1) | 2 x 1 = 2 |
| Q3 Trig | 3 | NORMAL (x2) | 3 x 2 = 6 |
| Q4 Trig | 3 | HARD (x3) | 3 x 3 = 9 |
| **Max score** | | | **23** |

If the player answers Q1 and Q4 correctly, the score is 6 + 9 = 15 / 23. The smallest possible max score is 10 (all EASY) and the largest is 30 (all HARD).

### Other rules
- Numbers less than 2 are not prime.
- The modulus divisor must not be zero (`calculateMod` throws a validation error).
- Trigonometry takes degrees and converts them to radians before using `Math.sin()`, `Math.cos()`, `Math.tan()`.
- Decimal answers are compared with a tolerance of 0.01 (round to 2 decimals).
- Tangent at 90 and 270 degrees is undefined: the player types `undefined`.
- Java `%` is used for modulus, so the sign follows the first number (`-17 % 5 = -2`).
- The `difficulty` column stores all four difficulties, for example `EASY,HARD,NORMAL,NORMAL`.

## Database Configuration
Settings are in `src/main/java/dao/ScoreDAO.java`:

| Setting | Default |
|---|---|
| URL | `jdbc:mysql://localhost:3306/math_whiz_db` |
| User | `root` |
| Password | empty. Set `PASS` in `ScoreDAO.java` to your own MySQL password |

```java
private static final String PASS = env("MW_DB_PASS", "your_password");
```

You can also use the environment variables `MW_DB_URL`, `MW_DB_USER` and `MW_DB_PASS` instead of editing the code.

## Database Creation SQL
File: `database/math_whiz_db.sql`

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

| Column | Type | Meaning |
|---|---|---|
| id | INT, PK, auto increment | row id |
| player_name | VARCHAR(100) | player name |
| score | INT | marks earned |
| max_score | INT | maximum marks of that game |
| difficulty | VARCHAR(50) | difficulty of Q1 to Q4 |
| played_at | TIMESTAMP | date and time of the game |

Leaderboard query (highest score first, earlier time wins a tie):
```sql
SELECT * FROM game_scores ORDER BY score DESC, played_at ASC LIMIT 10;
```

## Steps to Run the Application
1. Clone or download this repository.
2. Create the database: run `database/math_whiz_db.sql` in MySQL (Command Line Client, Workbench or phpMyAdmin).
3. Open the `MathWhiz` folder in IntelliJ IDEA as a Maven project and wait for the dependencies to download.
4. Set the project JDK to 17 or newer (*File, Project Structure, Project*).
5. Set your MySQL password in `ScoreDAO.java`.
6. Add a Tomcat run configuration: *Run, Edit Configurations, +, Tomcat Server, Local*. Choose your Tomcat 11 folder. On the *Deployment* tab add the artifact `MathWhiz:war exploded` and set the application context to `/MathWhiz`.
7. Click Run and open `http://localhost:8080/MathWhiz/`.

Alternative without IntelliJ: run `mvn clean package` and copy `target/MathWhiz.war` into Tomcat's `webapps` folder.

---

## Source Code

### pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.mathwhiz</groupId>
  <artifactId>MathWhiz</artifactId>
  <version>1.0</version>
  <packaging>war</packaging>

  <properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <failOnMissingWebXml>false</failOnMissingWebXml>
  </properties>

  <dependencies>
    <dependency>
      <groupId>jakarta.servlet</groupId>
      <artifactId>jakarta.servlet-api</artifactId>
      <version>6.1.0</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>jakarta.servlet.jsp</groupId>
      <artifactId>jakarta.servlet.jsp-api</artifactId>
      <version>4.0.0</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>jakarta.servlet.jsp.jstl</groupId>
      <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
      <version>3.0.2</version>
    </dependency>
    <dependency>
      <groupId>org.glassfish.web</groupId>
      <artifactId>jakarta.servlet.jsp.jstl</artifactId>
      <version>3.0.1</version>
    </dependency>
    <dependency>
      <groupId>com.mysql</groupId>
      <artifactId>mysql-connector-j</artifactId>
      <version>8.2.0</version>
    </dependency>
  </dependencies>

  <build>
    <finalName>MathWhiz</finalName>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-war-plugin</artifactId>
        <version>3.4.0</version>
      </plugin>
    </plugins>
  </build>
</project>
```

### MathService.java (math methods)
The required methods `isPrime`, `calculateMod`, `calculateSin`, `calculateCos`, `calculateTan`.

```java
package service;

public class MathService {

    public static final double TOLERANCE = 0.01;

    public static int isPrime(int number) {
        if (number < 2) return 0;
        for (int i = 2; (long) i * i <= number; i++)
            if (number % i == 0) return 0;
        return 1;
    }

    public static int calculateMod(int a, int b) {
        if (b == 0) throw new IllegalArgumentException("Divisor must not be zero");
        return a % b;
    }

    public static double calculateSin(double degree) {
        return Math.sin(Math.toRadians(degree));
    }

    public static double calculateCos(double degree) {
        return Math.cos(Math.toRadians(degree));
    }

    // returns NaN when tangent is undefined (90, 270, ...)
    public static double calculateTan(double degree) {
        double cos = Math.cos(Math.toRadians(degree));
        if (Math.abs(cos) < 1e-9) return Double.NaN;
        return Math.tan(Math.toRadians(degree));
    }

    public static boolean isClose(double given, double expected) {
        return Math.abs(given - expected) <= TOLERANCE;
    }
}
```

### Difficulty.java (multipliers)

```java
package model;

public enum Difficulty {
    EASY(1), NORMAL(2), HARD(3);

    private final int multiplier;

    Difficulty(int multiplier) { this.multiplier = multiplier; }

    public int getMultiplier() { return multiplier; }
}
```

### Question.java (marks formula and answer checking)
`getMarks()` is the marking formula. `isCorrect()` calls the `MathService` methods to check the answer.

```java
package model;

import service.MathService;

public class Question {

    public enum Type { PRIME, MOD, SIN, COS, TAN }

    private final Type type;
    private final int baseMark;
    private final Difficulty difficulty;
    private final int a, b;

    public Question(Type type, int baseMark, Difficulty difficulty, int a, int b) {
        this.type = type;
        this.baseMark = baseMark;
        this.difficulty = difficulty;
        this.a = a;
        this.b = b;
    }

    public Type getType() { return type; }
    public int getBaseMark() { return baseMark; }
    public Difficulty getDifficulty() { return difficulty; }

    // mark = question mark * marking difficulty
    public int getMarks() { return baseMark * difficulty.getMultiplier(); }

    public String getSection() {
        switch (type) {
            case PRIME: return "Prime Number";
            case MOD:   return "Modulus";
            default:    return "Trigonometry";
        }
    }

    public String getText() {
        switch (type) {
            case PRIME: return "Is " + a + " a prime number? (1 = Prime, 0 = Not Prime)";
            case MOD:   return "Calculate " + a + " % " + b + " (remainder)";
            default:    return "Calculate " + type.name().toLowerCase() + "(" + a
                              + "\u00B0), rounded to 2 decimals" + (type == Type.TAN ? " (or type: undefined)" : "");
        }
    }

    private double expectedTrig() {
        switch (type) {
            case SIN: return MathService.calculateSin(a);
            case COS: return MathService.calculateCos(a);
            default:  return MathService.calculateTan(a);
        }
    }

    public String getExpected() {
        switch (type) {
            case PRIME: return String.valueOf(MathService.isPrime(a));
            case MOD:   return String.valueOf(MathService.calculateMod(a, b));
            default:
                double e = expectedTrig();
                return Double.isNaN(e) ? "undefined" : String.format("%.2f", e);
        }
    }

    public boolean isCorrect(String answer) {
        String s = answer == null ? "" : answer.trim();
        try {
            switch (type) {
                case PRIME: return Integer.parseInt(s) == MathService.isPrime(a);
                case MOD:   return Integer.parseInt(s) == MathService.calculateMod(a, b);
                default:
                    double e = expectedTrig();
                    if (Double.isNaN(e)) return s.equalsIgnoreCase("undefined");
                    return MathService.isClose(Double.parseDouble(s), e);
            }
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
```

### QuestionResult.java

```java
package model;

public class QuestionResult {
    private final Question question;
    private final String userAnswer;
    private final boolean correct;

    public QuestionResult(Question question, String userAnswer) {
        this.question = question;
        this.userAnswer = userAnswer == null ? "" : userAnswer.trim();
        this.correct = question.isCorrect(userAnswer);
    }

    public Question getQuestion() { return question; }
    public String getUserAnswer() { return userAnswer; }
    public boolean isCorrect() { return correct; }
    public int getEarned() { return correct ? question.getMarks() : 0; }
}
```

### GameScore.java

```java
package model;

import java.sql.Timestamp;

public class GameScore {
    private int id;
    private String playerName;
    private int score;
    private int maxScore;
    private String difficulty;
    private Timestamp playedAt;

    public GameScore() {}

    public GameScore(String playerName, int score, int maxScore, String difficulty) {
        this.playerName = playerName;
        this.score = score;
        this.maxScore = maxScore;
        this.difficulty = difficulty;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getMaxScore() { return maxScore; }
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public Timestamp getPlayedAt() { return playedAt; }
    public void setPlayedAt(Timestamp playedAt) { this.playedAt = playedAt; }
}
```

### QuestionFactory.java (random questions with individual difficulty)

```java
package service;

import model.Difficulty;
import model.Question;
import model.Question.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionFactory {

    private static final Random R = new Random();

    // Q1 prime (2), Q2 mod (2), Q3 trig (3), Q4 trig (3); each gets its own difficulty
    public static List<Question> generate() {
        List<Question> list = new ArrayList<>();
        list.add(prime(randomDifficulty()));
        list.add(mod(randomDifficulty()));
        list.add(trig(randomDifficulty()));
        list.add(trig(randomDifficulty()));
        return list;
    }

    private static Difficulty randomDifficulty() {
        return Difficulty.values()[R.nextInt(Difficulty.values().length)];
    }

    private static Question prime(Difficulty d) {
        int n;
        switch (d) {
            case EASY:   n = 2 + R.nextInt(29);    break;
            case NORMAL: n = 30 + R.nextInt(171);  break;
            default:     n = 200 + R.nextInt(1800);
        }
        return new Question(Type.PRIME, 2, d, n, 0);
    }

    private static Question mod(Difficulty d) {
        int a, b;
        switch (d) {
            case EASY:
                a = 10 + R.nextInt(41);
                b = 2 + R.nextInt(8);
                break;
            case NORMAL:
                a = 50 + R.nextInt(451);
                b = 3 + R.nextInt(28);
                break;
            default:
                a = (R.nextBoolean() ? 1 : -1) * (100 + R.nextInt(900));
                b = (R.nextBoolean() ? 1 : -1) * (7 + R.nextInt(44));
        }
        return new Question(Type.MOD, 2, d, a, b);
    }

    private static Question trig(Difficulty d) {
        int[] easy = {0, 30, 60, 90};
        int[] normal = {0, 30, 45, 60, 90, 120, 135, 150, 180};
        int angle;
        Type t;
        switch (d) {
            case EASY:
                angle = easy[R.nextInt(easy.length)];
                t = R.nextBoolean() ? Type.SIN : Type.COS;
                break;
            case NORMAL:
                angle = normal[R.nextInt(normal.length)];
                t = Type.values()[2 + R.nextInt(3)];
                break;
            default:
                angle = 15 * R.nextInt(25);
                t = Type.values()[2 + R.nextInt(3)];
        }
        return new Question(t, 3, d, angle, 0);
    }
}
```

### ScoreDAO.java (JDBC)

```java
package dao;

import model.GameScore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    private static final String URL = env("MW_DB_URL",
            "jdbc:mysql://localhost:3306/math_whiz_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    private static final String USER = env("MW_DB_USER", "root");
    private static final String PASS = env("MW_DB_PASS", "");

    private static String env(String key, String def) {
        String v = System.getenv(key);
        return v == null ? def : v;
    }

    private Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL driver not found", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public void save(GameScore g) throws SQLException {
        String sql = "INSERT INTO game_scores (player_name, score, max_score, difficulty, played_at) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, g.getPlayerName());
            ps.setInt(2, g.getScore());
            ps.setInt(3, g.getMaxScore());
            ps.setString(4, g.getDifficulty());
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
    }

    public List<GameScore> top10() throws SQLException {
        String sql = "SELECT id, player_name, score, max_score, difficulty, played_at "
                   + "FROM game_scores ORDER BY score DESC, played_at ASC LIMIT 10";
        List<GameScore> list = new ArrayList<>();
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                GameScore g = new GameScore();
                g.setId(rs.getInt("id"));
                g.setPlayerName(rs.getString("player_name"));
                g.setScore(rs.getInt("score"));
                g.setMaxScore(rs.getInt("max_score"));
                g.setDifficulty(rs.getString("difficulty"));
                g.setPlayedAt(rs.getTimestamp("played_at"));
                list.add(g);
            }
        }
        return list;
    }
}
```

### GameServlet.java (controller)

```java
package controller;

import dao.ScoreDAO;
import model.GameScore;
import model.Question;
import model.QuestionResult;
import service.QuestionFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/start", "/submit", "/leaderboard"})
public class GameServlet extends HttpServlet {

    private final ScoreDAO dao = new ScoreDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/leaderboard".equals(req.getServletPath())) {
            try {
                req.setAttribute("scores", dao.top10());
            } catch (SQLException e) {
                throw new ServletException("Database error", e);
            }
            req.getRequestDispatcher("/leaderboard.jsp").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if ("/start".equals(req.getServletPath())) start(req, resp);
        else submit(req, resp);
    }

    private void start(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("playerName");
        name = name == null ? "" : name.trim();
        if (name.isEmpty() || name.length() > 100) {
            req.setAttribute("error", "Enter a player name (1-100 characters).");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }
        HttpSession session = req.getSession();
        session.setAttribute("player", name);
        session.setAttribute("questions", QuestionFactory.generate());
        req.getRequestDispatcher("/game.jsp").forward(req, resp);
    }

    @SuppressWarnings("unchecked")
    private void submit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        List<Question> questions = session == null ? null : (List<Question>) session.getAttribute("questions");
        if (questions == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        String player = (String) session.getAttribute("player");

        List<QuestionResult> results = new ArrayList<>();
        int score = 0, max = 0;
        for (int i = 0; i < questions.size(); i++) {
            QuestionResult r = new QuestionResult(questions.get(i), req.getParameter("ans" + i));
            results.add(r);
            score += r.getEarned();
            max += questions.get(i).getMarks();
        }
        String difficulty = questions.stream().map(q -> q.getDifficulty().name()).collect(Collectors.joining(","));

        try {
            dao.save(new GameScore(player, score, max, difficulty));
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }

        session.removeAttribute("questions");
        req.setAttribute("player", player);
        req.setAttribute("results", results);
        req.setAttribute("score", score);
        req.setAttribute("maxScore", max);
        req.getRequestDispatcher("/result.jsp").forward(req, resp);
    }
}
```

### index.jsp (Welcome page)

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>WASI – MATH WHIZ</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="card">
  <h1>WASI – MATH WHIZ</h1>
  <p>4 questions. Each question has its own difficulty: <b>EASY ×1</b>, <b>NORMAL ×2</b>, <b>HARD ×3</b>.</p>
  <c:if test="${not empty error}"><p class="err"><c:out value="${error}"/></p></c:if>
  <form action="${pageContext.request.contextPath}/start" method="post">
    <label>Player Name</label>
    <input type="text" name="playerName" maxlength="100" required>
    <button type="submit">Start Game</button>
  </form>
  <a href="${pageContext.request.contextPath}/leaderboard">View Leaderboard</a>
</div>
</body>
</html>
```

### game.jsp (Game page)

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>WASI – MATH WHIZ | Game</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="card">
  <h1>WASI – MATH WHIZ</h1>
  <p>Player: <b><c:out value="${sessionScope.player}"/></b></p>
  <form action="${pageContext.request.contextPath}/submit" method="post">
    <c:forEach var="q" items="${sessionScope.questions}" varStatus="s">
      <div class="q">
        <div class="meta">
          Q${s.count} · ${q.section} ·
          <span class="tag ${q.difficulty}">${q.difficulty}</span> ·
          ${q.baseMark} × ${q.difficulty.multiplier} = <b>${q.marks} marks</b>
        </div>
        <div><c:out value="${q.text}"/></div>
        <input type="text" name="ans${s.index}" required>
      </div>
    </c:forEach>
    <button type="submit">Submit Answers</button>
  </form>
</div>
</body>
</html>
```

### result.jsp (Result page)

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>WASI – MATH WHIZ | Result</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="card">
  <h1>WASI – MATH WHIZ</h1>
  <h2><c:out value="${player}"/>: ${score} / ${maxScore}</h2>
  <table>
    <tr><th>Q</th><th>Difficulty</th><th>Your Answer</th><th>Correct</th><th>Marks</th></tr>
    <c:forEach var="r" items="${results}" varStatus="s">
      <tr class="${r.correct ? 'ok' : 'bad'}">
        <td>${s.count}</td>
        <td>${r.question.difficulty} (×${r.question.difficulty.multiplier})</td>
        <td><c:out value="${r.userAnswer}"/></td>
        <td>${r.question.expected}</td>
        <td>${r.earned} / ${r.question.marks}</td>
      </tr>
    </c:forEach>
  </table>
  <a href="${pageContext.request.contextPath}/leaderboard">Leaderboard</a> |
  <a href="${pageContext.request.contextPath}/">Play Again</a>
</div>
</body>
</html>
```

### leaderboard.jsp (Top 10)

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>WASI – MATH WHIZ | Leaderboard</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="card wide">
  <h1>Top 10 Leaderboard</h1>
  <table>
    <tr><th>#</th><th>Player</th><th>Score</th><th>Difficulty (Q1-Q4)</th><th>Date/Time</th></tr>
    <c:forEach var="g" items="${scores}" varStatus="s">
      <tr>
        <td>${s.count}</td>
        <td><c:out value="${g.playerName}"/></td>
        <td>${g.score} / ${g.maxScore}</td>
        <td>${g.difficulty}</td>
        <td><fmt:formatDate value="${g.playedAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
      </tr>
    </c:forEach>
    <c:if test="${empty scores}"><tr><td colspan="5">No scores yet.</td></tr></c:if>
  </table>
  <a href="${pageContext.request.contextPath}/">Play Game</a>
</div>
</body>
</html>
```

### MathServiceTest.java (tests)

```java
package service;

import model.Difficulty;
import model.Question;
import model.Question.Type;

public class MathServiceTest {

    static int fail = 0;

    static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS " : "FAIL ") + name);
        if (!ok) fail++;
    }

    public static void main(String[] args) {
        check("prime 7 -> 1", MathService.isPrime(7) == 1);
        check("prime 8 -> 0", MathService.isPrime(8) == 0);
        check("prime 1 -> 0", MathService.isPrime(1) == 0);
        check("prime -5 -> 0", MathService.isPrime(-5) == 0);
        check("17 % 5 -> 2", MathService.calculateMod(17, 5) == 2);
        check("-17 % 5 -> -2", MathService.calculateMod(-17, 5) == -2);
        check("17 % -5 -> 2", MathService.calculateMod(17, -5) == 2);
        boolean thrown = false;
        try { MathService.calculateMod(5, 0); } catch (IllegalArgumentException e) { thrown = true; }
        check("divisor 0 -> validation error", thrown);
        check("sin 30 -> 0.5", MathService.isClose(MathService.calculateSin(30), 0.5));
        check("cos 60 -> 0.5", MathService.isClose(MathService.calculateCos(60), 0.5));
        check("tan 45 -> 1.0", MathService.isClose(MathService.calculateTan(45), 1.0));
        check("tan 90 -> undefined", Double.isNaN(MathService.calculateTan(90)));

        // marking: mark = question mark * difficulty multiplier
        check("prime HARD = 2*3 = 6", new Question(Type.PRIME, 2, Difficulty.HARD, 7, 0).getMarks() == 6);
        check("trig NORMAL = 3*2 = 6", new Question(Type.SIN, 3, Difficulty.NORMAL, 30, 0).getMarks() == 6);
        check("trig EASY = 3*1 = 3", new Question(Type.COS, 3, Difficulty.EASY, 60, 0).getMarks() == 3);
        check("answer check prime", new Question(Type.PRIME, 2, Difficulty.EASY, 7, 0).isCorrect("1"));
        check("wrong answer rejected", !new Question(Type.MOD, 2, Difficulty.EASY, 17, 5).isCorrect("3"));
        check("tan 90 'undefined' accepted", new Question(Type.TAN, 3, Difficulty.HARD, 90, 0).isCorrect("undefined"));
        check("non-numeric rejected", !new Question(Type.SIN, 3, Difficulty.EASY, 30, 0).isCorrect("abc"));

        System.out.println(fail == 0 ? "ALL PASSED" : fail + " FAILED");
    }
}
```

---

## Testing and Validation

`MathServiceTest` runs these checks. Run it from IntelliJ (right click, *Run MathServiceTest.main()*); it prints `PASS` or `FAIL` for each case.

| Test Case | Expected Result |
|---|---|
| Prime input: 7 | 1 |
| Prime input: 8 | 0 |
| Prime input: 1 | 0 |
| Prime input: -5 | 0 |
| Modulus: 17 % 5 | 2 |
| Modulus: -17 % 5 | -2 |
| Sine: 30 degrees | 0.5 |
| Cosine: 60 degrees | 0.5 |
| Tangent: 45 degrees | 1.0 |
| Tangent: 90 degrees | undefined |
| Divisor: 0 | Validation error |
| Prime HARD (2 x 3) | 6 marks |
| Trig NORMAL (3 x 2) | 6 marks |
| Trig EASY (3 x 1) | 3 marks |
| All answers correct | Maximum score of that game |
| Incorrect answers | Marks of those questions not awarded |

## Troubleshooting

| Problem | Fix |
|---|---|
| `NoClassDefFoundError: javax/servlet/...` or JSTL error | Use Tomcat 10 or newer. This project uses `jakarta.*` and is built for Tomcat 11. |
| `Address localhost:8080 is already in use` | Stop the old Tomcat (red Stop button), or change the HTTP port in the Tomcat run configuration. |
| `Access denied for user 'root'@'localhost'` | Set the correct MySQL password in `ScoreDAO.java` and restart Tomcat. |
| `Unknown database 'math_whiz_db'` | Run `database/math_whiz_db.sql` first. |
| `MySQL driver not found` | Reload Maven (elephant icon in the Maven tab). |
| `Communications link failure` | Start the MySQL service. |


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
