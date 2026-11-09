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
