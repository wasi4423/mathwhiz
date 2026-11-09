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
