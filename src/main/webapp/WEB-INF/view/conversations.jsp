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
<%@ page import="java.util.List"%>
<%@ page import="codeu.model.data.Conversation"%>
<%@ page import="codeu.model.data.User"%>
<%@ page import="codeu.model.store.basic.UserStore"%>
<%@ page import="java.util.Arrays"%>

<!DOCTYPE html>
<html>
<head>
<title>Conversations</title>
  <link rel="stylesheet" href="/css/main.css">
  <link rel="import" href="/index.jsp">
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

		<%
		  if (request.getAttribute("error") != null) {
		%>
		<h2 style="color: red"><%=request.getAttribute("error")%></h2>
		<%
		  }
		%>


    <% if(request.getSession().getAttribute("user") != null){ %>
      <script type="text/javascript" src="/js/main.js"></script>

    <h1>New Conversation</h1>
      <form action="/conversations" method="POST">
          <div class="form-group">
            <label class="form-control-label">Title:</label>
          <input type="text" name="conversationTitle">
        </div>


			<button type="submit">Create</button>
		</form>

		<hr />
		<%
		  }
		%>

		<h1>Conversations</h1>

		<%
		  List<Conversation> conversations = (List<Conversation>) request.getAttribute("conversations");
		  if (conversations == null || conversations.isEmpty()) {
		%>
		<p>Create a conversation to get started.</p>
		<%
		  } else {
		%>
		<ul class="mdl-list">
			<%
			  for (Conversation conversation : conversations) {
			%>
			<li><a href="/chat/<%=conversation.getTitle()%>"> <%=conversation.getTitle()%></a>
				<% if (request.getSession().getAttribute("user") != null) {
				%>
				<form method='post'>
					<input type="hidden" value=<%=conversation.getTitle()%>
						name="hiddenConversationTitle" />
					<%
					  String username = (String) request.getSession().getAttribute("user");
					      User currentUser = UserStore.getInstance().getUser(username);
					      if (currentUser.isConversationUnfollowed(conversation)) {
					%>
					<input align="right" type="submit" id="submit" value=Follow name="Unfollowing"
						id="unfollowing" />
					<%
					  } else {
					%>
					<input align="right" type="submit" id="submit" value=Unfollow name="Following"
						id="following" />
				</form>
					<%
					}
					}
					}
					%>
		</ul>
		<%
			}
		%>
		<hr />
	</div>
</body>
</html>
