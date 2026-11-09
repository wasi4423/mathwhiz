package dao;

import model.GameScore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    private static final String URL = env("MW_DB_URL",
            "jdbc:mysql://localhost:3306/math_whiz_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    private static final String USER = env("MW_DB_USER", "root");
    private static final String PASS = env("MW_DB_PASS", "wasi");

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
