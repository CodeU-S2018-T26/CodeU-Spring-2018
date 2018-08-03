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
<%@ page import="java.util.ArrayList" %>


<%@ page import="com.google.appengine.api.datastore.Blob"%>

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
      background-color:#2d2d2d;
      border: 5px solid #00CED1;
      border-radius: 10px;
      margin-top: 10px;
      margin-bottom: 10px;
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

    function displayShortcodeEntry() {
      var checkBox = document.getElementById("emoji-checkbox");
    // Get the output text
      var messageTextBox = document.getElementById("text");
      var shortcodeTextBox = document.getElementById("shortcode");
      var emojiPrompt = document.getElementById("emoji-prompt");
      if(checkBox.checked==true){
        shortcodeTextBox.style.display = "inline";
        messageTextBox.style.display = "none";
        emojiPrompt.style.display = "none";
      } else {
        shortcodeTextBox.style.display = "none";
        messageTextBox.style.display = "inline";
        emojiPrompt.style.display = "inline";

      }
    };
  </script>
</head>
<body onload="scrollChat()">

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

    <h1><%= conversation.getTitle() %>
      <a href="" style="float: right">&#8635;</a></h1>

    <hr/>

    <div id="chat">
      <ul>
      <%
        for (Message message : messages) {
          String author = UserStore.getInstance()
            .getUser(message.getAuthorId()).getName();
          String messageContent = message.getContent();
      %>
        <li style="color: #00CED1"><strong><%= author %>:</strong>
        <%-- Prints out strings of message parts, inserting custom emojis --%>
        <%-- wherever there is a '|' character --%>
        <%
          int emojiLocation = 0;
          String subMessage = "";
          for (char messageChar : messageContent.toCharArray()){
            if (messageChar == '|' && message.emojisExist()){
              ArrayList<Blob> blobs = message.getEmojis();
              Blob currentBlob = blobs.get(emojiLocation);
              String b64 = javax.xml.bind.DatatypeConverter.printBase64Binary(currentBlob.getBytes());
              emojiLocation += 1;
        %>
              <p style="color: #00CED1; display:inline"><%=subMessage%></p>
              <img src="data:image/jpg;base64, <%=b64%>" alt="Image not found"/>
        <%
              subMessage = "";
            }else{
              subMessage += messageChar;
            }
          }
        %>
        <p style="color: #00CED1; display:inline"><%=subMessage%></p>
        </li>
        <%if (message.imageExists()) {
             Blob blob = message.getImage();
             String b64 = javax.xml.bind.DatatypeConverter.printBase64Binary(blob.getBytes());
             %>
             <img src="data:image/jpg;base64, <%=b64%>" alt="Image not found"/>
    <%    }
      }
    %>
      </ul>
    </div>
    <hr/>
    <% if (request.getSession().getAttribute("user") != null) { %>
    <form action="/chat/<%= conversation.getTitle() %>" enctype="multipart/form-data" method="POST">
        <input type="text" style="display:inline" name="message" placeholder="message">
        <input type="file" accept="image/*" name="image">
        <input type="checkbox" id="emoji-checkbox" name="emoji-checkbox" onclick="displayShortcodeEntry()">
        <p id="emoji-prompt" style="display:inline; font-size:14px"> Upload custom emoji?</p>
        <input type="text" style="display:none" id="shortcode" name="shortcode" placeholder="emoji name">
        <br/>
        <button type="submit">Send</button>
    </form>
    <% } else { %>
      <p><a href="/login">Login</a> to send a message.</p>
    <% } %>
    <hr/>
  </div>
</body>
</html>
