<%--
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
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="java.util.Arrays" %>

<%@ page import="com.google.appengine.api.datastore.Blob"%>
<%-- <%@ page import="java.awt.image.BufferedImage"%> --%>
<%-- <%@ page import="org.apache.commons.io.IOUtils"%> --%>
<%-- <%@ page import="java.io.ByteArrayOutputStream"%> --%>

<%
Conversation conversation = (Conversation) request.getAttribute("conversation");
List<Message> messages = (List<Message>) request.getAttribute("messages");
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= conversation.getTitle() %></title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">

  <style>
    #chat {
      background-color: white;
      height: 500px;
      overflow-y: scroll
    }
  </style>

  <script>
    // scroll the chat div to the bottom
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };
  </script>
</head>
<body onload="scrollChat()">

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
      <% if (request.getSession().getAttribute("user") != null) { %>
    <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <%if((boolean) request.getSession().getAttribute("isAdmin") == true){%>
    <a href = "/admin">Admin</a><%}
      } else { %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

  <div id="container">

    <h1><%= conversation.getTitle() %>
      <a href="" style="float: right">&#8635;</a></h1>

    <hr/>

    <div id="chat">
      <ul>
      <%
        for (Message message : messages) {
          String author = UserStore.getInstance()
            .getUser(message.getAuthorId()).getName();
      %>
        <li><strong><%= author %>:</strong> <%= message.getContent() %> </li>
        <%if (message.imageExists()) {
             Blob blob = message.getImage();
             String b64 = javax.xml.bind.DatatypeConverter.printBase64Binary(blob.getBytes());
             %>

             <img src="data:image/jpg;base64, <%=b64%>" alt="Image not found" />

      <%} %>

    <%
      }
    %>
      </ul>
    </div>

    <hr/>

    <% if (request.getSession().getAttribute("user") != null) { %>
    <form action="/chat/<%= conversation.getTitle() %>" enctype="multipart/form-data" method="POST">
        <input type="text" name="message">
        <input type="file" accept="image/*" name="image">
        <br/>
        <button type="submit">Send</button>
    </form>
    <%-- <form method='post' enctype='multipart/form-data' action='/file-upload'>
        <input type='file' name='thumbnail' />
        <input type='hidden' name='base64data' />
        <input type='submit' formenctype='application/x-www-form-urlencoded' />
    </form> --%>
    <% } else { %>
      <p><a href="/login">Login</a> to send a message.</p>
    <% } %>

    <hr/>

  </div>

</body>
</html>
