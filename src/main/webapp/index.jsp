<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Clinic</title>
</head>
<body>

<% if (session.getAttribute("user") != null) { %>

  <h1>Welcome ${sessionScope.user.prenom} ${sessionScope.user.nom}</h1>
  <p>Signed in as ${sessionScope.user.email} (${sessionScope.role})</p>
  <a href="${pageContext.request.contextPath}/users/logout">Sign out</a>

<% } else { %>

  <h1>Clinic Management</h1>
  <a href="${pageContext.request.contextPath}/users/login">Sign in</a>
  <a href="${pageContext.request.contextPath}/users/create">Sign up</a>

<% } %>

</body>
</html>
