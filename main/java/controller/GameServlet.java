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
