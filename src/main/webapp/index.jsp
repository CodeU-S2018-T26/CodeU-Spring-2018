<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %><%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<!DOCTYPE html>
<html>
<head>
  <title>JRAMM Chat</title>
  <link rel="stylesheet" href="/css/main.css">
  <%-- for testing --%>
  <meta http-equiv="Cache-control" content="no-cache">

  <script src="https://www.gstatic.com/firebasejs/5.0.4/firebase-app.js"></script>
  <script src="https://www.gstatic.com/firebasejs/5.0.4/firebase-messaging.js"></script>
  <script src="https://www.gstatic.com/firebasejs/5.0.4/firebase-database.js"></script>
  <script>
      // Initialize Firebase
      var config = {
          apiKey: "AIzaSyDW8JwUxULiEp0YMb0HCQMHps5rXNamyRQ",
          authDomain: "projecteam26.firebaseapp.com",
          databaseURL: "https://projecteam26.firebaseio.com",
          projectId: "projecteam26",
          storageBucket: "projecteam26.appspot.com",
          messagingSenderId: "290994051645"
      };
      firebase.initializeApp(config);
  </script>
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

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1>JRAMM Chat</h1>
      <h2>CodeU Chat App</h2>
      <h3>Welcome!</h3>

      <ul>
        <li><a href="/login">Login</a> to get started.</li>
        <li>Go to the <a href="/conversations">conversations</a> page to
            create or join a conversation.</li>
        <li>View the <a href="/about.jsp">about</a> page to learn more about the
            project.</li>
      </ul>
    </div>
  </div>
</body>
</html>
