<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Clinic - Sign in</title>
</head>
<body>
<h1>Sign in</h1>

<% if (request.getAttribute("error") != null) { %>
<p role="alert"><%= request.getAttribute("error") %></p>
<% } %>

<form method="post" action="${pageContext.request.contextPath}/users/login">

  <p>
    <label for="email">Email</label>
    <input type="email" id="email" name="email" value="${param.email}" required>
  </p>

  <p>
    <label for="password">Password</label>
    <input type="password" id="password" name="password" required>
  </p>

  <p>
    <button type="submit">Sign in</button>
  </p>

</form>

<p>No account yet? <a href="${pageContext.request.contextPath}/users/create">Sign up</a></p>
</body>
</html>
