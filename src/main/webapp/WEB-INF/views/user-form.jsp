<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Clinic - Sign up</title>
</head>
<body>
<h1>Create an account</h1>

<% if (request.getAttribute("error") != null) { %>
<p role="alert"><%= request.getAttribute("error") %></p>
<% } %>

<form method="post" action="${pageContext.request.contextPath}/users/create">

  <p>
    <label for="nom">Last name</label>
    <input type="text" id="nom" name="nom" value="${param.nom}" required>
  </p>

  <p>
    <label for="prenom">First name</label>
    <input type="text" id="prenom" name="prenom" value="${param.prenom}" required>
  </p>

  <p>
    <label for="email">Email</label>
    <input type="email" id="email" name="email" value="${param.email}" required>
  </p>

  <p>
    <label for="telephone">Phone number</label>
    <input type="tel" id="telephone" name="telephone" value="${param.telephone}" required>
  </p>

  <p>
    <label for="password">Password</label>
    <input type="password" id="password" name="password" minlength="6" required>
  </p>

  <p>
    <button type="submit">Sign up</button>
  </p>

</form>

<p>Already registered? <a href="${pageContext.request.contextPath}/users/login">Sign in</a></p>
</body>
</html>
