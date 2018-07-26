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
  <title>CodeU Chat App</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">JRAMM Chat</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <%if((boolean) request.getSession().getAttribute("isAdmin") == true){%>
      <a href = "/admin">Admin</a><%}
       } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1>About our CodeU Chat App</h1>
      <h3>JRAMM</h3>
      <p>
        Our chat app has a few interesting features, namely
        <strong>Notifications</strong>, an <strong>Activity Feed</strong>,
        the ability to <strong>Send Images</strong>,
        and <strong>Custom Emojis.</strong>
      </p>

      <ul>
        <li><strong>Notifications:</strong> Using the Firebase API, our app
        pushes notifications to the user's browser whenever a new message
        is sent in a conversation they are subscribed to. For demonstration
        purposes, you will recieve notifications even for messages that you
        send. This way you can actually see notifications in action!</li>
        <li><strong>Activity Feed:</strong> The Activity Feed allows the user
        to see what's going on in the conversations that they follow. You can
        when new users join the chat and send messages.</li>
        <li><strong>Images and Custom Emojis:</strong> When composing a message,
        the user has the option to attach and send any image. That image is
        then saved with the message in DataStore. Additionally, you can create
        custom emojis by uploading a photo and giving it a "shortcode" name.
        Custom emojis are sent by typing the :shortcode: surrounded by the ':'
        character. You can also send standaard emojis by using the shortcodes
        <a href="https://www.webpagefx.com/tools/emoji-cheat-sheet/" target="blank_">here</a>.</li>
      </ul>
    </div>
  </div>
</body>
</html>
