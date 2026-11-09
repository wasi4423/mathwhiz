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
