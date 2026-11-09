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
