<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %><%--

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Admin Page</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

<nav>
  <a id="navTitle" href="/">CodeU Chat App</a>
  <a href="/conversations">Conversations</a>
  <% if (request.getSession().getAttribute("user") != null) { %>
  <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
  <%List<String> admins = Arrays.asList("ayliana", "Marouane", "jeremy", "marissa", "raymond");
    if(admins.contains(request.getSession().getAttribute("user"))){%>
  <a href = "/admin">Admin</a><%}
  } else { %>
  <a href="/login">Login</a>
  <% } %>
  <a href="/about.jsp">About</a>
</nav>

  <% String username = (String)request.getSession().getAttribute("user");
  if(request.getAttribute("error") != null){ %>
  <%-- displays error message if user is not an admin --%>
  <h1 style="color:red"><%= request.getAttribute("error") %></h1>
  <% }
  else { %>
    <h1>ADMIN PAGE!!!</h1>
  <%}%>

</body>
</html>
