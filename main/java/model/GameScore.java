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
