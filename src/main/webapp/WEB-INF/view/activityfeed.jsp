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
<%@ page import="java.util.*" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.time.*" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%@ page import="codeu.model.store.basic.MessageStore" %>
<!DOCTYPE html>
<html>
<head>
  <title>Activity Feed</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

	<nav>
		<a id="navTitle" href="/">CodeU Chat App</a> <a href="/conversations">Conversations</a>
		<a href="/about.jsp">About</a>
	</nav>

	<div id="container">
		<h1>Activity</h1>

		<p>This is the activity feed page</p>
		<ul>
		<%
			HashMap<Instant,HashMap<UUID,String>> instantByInstance = (HashMap<Instant,HashMap<UUID,String>>) request.getAttribute("instantByInstance");
			if (instantByInstance == null ) {
				return;
			} else {
				String author;
				Instant time;
				String title;
				for (Map.Entry<Instant,HashMap<UUID,String>> m:instantByInstance.entrySet()) {
					HashMap<UUID,String> innerm = m.getValue();
					System.out.println("printing..");
					System.out.println("Instant:");
					System.out.println(m.getKey().toString());
					for (Map.Entry<UUID,String> im:innerm.entrySet()) {
						System.out.println("UUID:");
						System.out.println(im.getKey());
						System.out.println("Section:");
						System.out.println(im.getValue());
						if ( im.getValue() == "user") { 
							author = UserStore.getInstance().getUser(im.getKey()).getName();
							time = UserStore.getInstance().getUser(im.getKey()).getCreationTime();
					
					%>
						<li><%=time.toString()%>: <%=author%> joined!</li>	
			<%
						}
						else if ( im.getValue() == "conversation") { 
							Conversation conversation = ConversationStore.getInstance().getConversation(im.getKey());
							author = UserStore.getInstance().getUser(conversation.getOwnerId()).getName();
							time = conversation.getCreationTime();
							title = conversation.getTitle();
						
					%>
						<li><%=time.toString()%>: <%=author%> created a new conversation: <a href="/chat/<%= title %>"><%= title %></a></li>
		<%
						}
						else if (im.getValue() == "message") {
							Message message = MessageStore.getInstance().getMessage(im.getKey());
					        author = UserStore.getInstance().getUser(message.getAuthorId()).getName();
					        String conversationTitle = ConversationStore.getInstance().getConversation(message.getConversationId()).getTitle();
					 %>
					 	<li><%=m.getKey()%>:  <%=author%> sent a message in <a href="/chat/<%=conversationTitle%>"><%=conversationTitle%></a>: "<%=message.getContent()%>"</li>
		<%
						}
		%>
		
		<%
} }
/* 			for (Map.Entry<Instant,HashMap<UUID,String>> m:instantByInstance.entrySet()) {
					HashMap<UUID,String> innerm = m.getValue();
					System.out.println("printing..");
					System.out.println("Instant:");
					System.out.println(m.getKey().toString());
					for (Map.Entry<UUID,String> im:innerm.entrySet()) {	
						System.out.println("UUID:");
						System.out.println(im.getKey());
						System.out.println("Section:");
						System.out.println(im.getValue());
			}
			} */
			}
%>
		</ul>
	</div>
</body>
</html>
