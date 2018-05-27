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
<%@ page import="java.util.Arrays" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.User" %>
<!DOCTYPE html>
<html>
<head>
  <title>Activity Feed</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <a href="/about.jsp">About</a>
  </nav>

  <div id="container">
    <h1>Activity</h1>

  		<p>This is the activity feed page</p>
		<%
			List<Conversation> conversations = (List<Conversation>) request.getAttribute("conversations");
			if (conversations == null || conversations.isEmpty()) {
		%>
		<li>There are no conversations created.</li>
		<%
			} else {
		%>
		<ul>
			<%
				for (Conversation conversation : conversations) {
			%>
			<li><%=conversation.getCreationTime()%>: Someone create a new conversation: <a href="/chat/<%= conversation.getTitle() %>"><%= conversation.getTitle() %></a></li>
			<%
				}
			%>
		</ul>
		<%
}
%>
		<%
			List<User> users = (List<User>) request.getAttribute("users");
			if (users == null || users.isEmpty()) {
		%>
		<li>There are no users signed up in the chat app.</li>
		<%
			} else {
		%>
		<ul>
			<%
				for (User user : users) {
			%>
			<li><%=user.getCreationTime()%>: <%=user.getName()%> joined!</li>
			<%
				}
			%>
		</ul>
		<%
}
%>

	</div>
</body>
</html>
