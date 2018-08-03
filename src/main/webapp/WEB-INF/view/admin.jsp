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
  <a href="/"><img src="/icon.png" width="96" height="96" hspace="10" vspace="10" alt="JRAMM Chat"></a>
  <% if(request.getSession().getAttribute("user") != null){ %>
    <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
  <%if((boolean) request.getSession().getAttribute("isAdmin") == true){%>
    <a href = "/admin">Admin</a><%}
      } else{ %>
    <a href="/login">Login</a>
  <% } %>
  <a href="/conversations">Conversations</a>
  <a href="/activity">Feed</a>
  <a href="/about.jsp">About</a>
</nav>

  <% String username = (String)request.getSession().getAttribute("user");
  if(request.getAttribute("error") != null){ %>
  <%-- displays error message if user is not an admin --%>
  <h1 style="color:red"><%= request.getAttribute("error") %></h1>
  <% }
  else { %>
    <h1>ADMIN PAGE!!!</h1>
    <h3> Number of Users: <%= request.getAttribute("numUsers") %></h3>
    <h3> Number of Conversations: <%= request.getAttribute("numConversations") %></h3>
    <h3> Number of Messages: <%= request.getAttribute("numMessages") %></h3>
    <h3> Newest User: <%= request.getAttribute("newestUser") %></h3>
    <h3> Most Active User: <%= request.getAttribute("mostActiveUser") %></h3>
    <h3> Wordiest User: <%= request.getAttribute("wordiestUser") %></h3>
  <%}%>

</body>
</html>
