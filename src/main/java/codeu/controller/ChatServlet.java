// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;



/** Servlet class responsible for the chat page. */
public class ChatServlet extends HttpServlet {

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  private Map<String, String> validEmojis = new HashMap<>();

  /** Set up state for handling chat requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());

    JSONParser parser = new JSONParser();
    try{
        Object obj = parser.parse(new FileReader("/Users/Raymond/Developer/CodeU/codeu_project_2018/src/main/resources/emoji/emojis.json"));
        JSONObject jsonObject = (JSONObject) obj;
        // loop array
        JSONArray emojis = (JSONArray) jsonObject.get("emojis");
        Iterator<JSONObject> iterator = emojis.iterator();
        while (iterator.hasNext()) {
            JSONObject emoji = iterator.next();
            String shortname = (String) emoji.get("shortname");
            String htmlCode = (String) emoji.get("html");
            //System.out.println(shortname);
            if (shortname != null && !shortname.isEmpty() && shortname.length() > 2){
                //System.out.println(shortname.substring(1, shortname.length()));
                validEmojis.put(shortname.substring(1, shortname.length()-1), htmlCode);
            }
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (ParseException e) {
        e.printStackTrace();
    }
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * This function fires when a user navigates to the chat page. It gets the conversation title from
   * the URL, finds the corresponding Conversation, and fetches the messages in that Conversation.
   * It then forwards to chat.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/chat/".length());

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      System.out.println("Conversation was null: " + conversationTitle);
      response.sendRedirect("/conversations");
      return;
    }

    UUID conversationId = conversation.getId();

    List<Message> messages = messageStore.getMessagesInConversation(conversationId);

    request.setAttribute("conversation", conversation);
    request.setAttribute("messages", messages);
    request.getRequestDispatcher("/WEB-INF/view/chat.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the chat page. It gets the logged-in
   * username from the session, the conversation title from the URL, and the chat message from the
   * submitted form data. It creates a new Message from that data, adds it to the model, and then
   * redirects back to the chat page.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      // user was not found, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/chat/".length());

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      response.sendRedirect("/conversations");
      return;
    }

    String messageContent = request.getParameter("message");

    // this removes any HTML from the message content
    String cleanedMessageContent = Jsoup.clean(messageContent, Whitelist.none());

    int cleanedMessageLength = cleanedMessageContent.length();
    boolean inItalics = false;
    boolean inBold = false;
    ArrayList<String> tokenizedMessageContent = new ArrayList();
    String parsedMessageContent = "";
    List<Character> validCharFlags = Arrays.asList('*', '_', '`');
    String emojiFlag = ":";
    List<String> validStrFlags = Arrays.asList("*", "_", "`", "**", "__");
    List<String> linkPrefix = Arrays.asList("http://", "https://", "www.");

    Map<String, String[]> markToHtml = new HashMap<>();

    markToHtml.put("*", new String[]{"<em>", "</em>"});
    markToHtml.put("_", new String[]{"<em>", "</em>"});
    markToHtml.put("`", new String[]{"<code>", "</code>"});
    markToHtml.put("**", new String[]{"<strong>", "</strong>"});
    markToHtml.put("__", new String[]{"<strong>", "</strong>"});
    markToHtml.put("LINK", new String[]{"<a href=\"", "\" target=\"_blank\">","</a>"});

    //validEmojis.put("hamburger", "&#x1F354");


    // tokenizes message into array list of strings
    for (int i = 0; i < cleanedMessageLength; i++) {

        if (validCharFlags.contains(cleanedMessageContent.charAt(i))){
            if (i+1 < cleanedMessageLength && validStrFlags.contains("" + cleanedMessageContent.charAt(i) + cleanedMessageContent.charAt(i+1))){
                tokenizedMessageContent.add(""+cleanedMessageContent.charAt(i)+cleanedMessageContent.charAt(i));
                i++;
            }
            else{
                tokenizedMessageContent.add(""+cleanedMessageContent.charAt(i));
            }
        }
        else{
            boolean inLink = false;
            for (String prefix: linkPrefix){
                if(i + prefix.length() < cleanedMessageLength && prefix.equals(cleanedMessageContent.substring(i, i+prefix.length()))) {
                    tokenizedMessageContent.add(prefix);
                    i += prefix.length()-1;
                    inLink = true;

                }
            }
            if(!inLink){
                tokenizedMessageContent.add(""+cleanedMessageContent.charAt(i));
            }
        }

    }

    // matches valid pairs of tokens and replaces with html syntax
    for (int i = 0; i < tokenizedMessageContent.size(); i++){
        if (validStrFlags.contains(tokenizedMessageContent.get(i))){
            for (int j = tokenizedMessageContent.size() - 1; j > i; j--){
                if(tokenizedMessageContent.get(i).equals(tokenizedMessageContent.get(j))){
                    String mark = tokenizedMessageContent.get(i);
                    tokenizedMessageContent.set(i, markToHtml.get(mark)[0]);
                    tokenizedMessageContent.set(j, markToHtml.get(mark)[1]);
                    break;
                }
            }
        }
        if (emojiFlag.equals(tokenizedMessageContent.get(i))){
            String shortcode = "";
            Boolean validSyntax = false;
            int j;
            for (j = i+1; j < tokenizedMessageContent.size(); j++){
                if(emojiFlag.equals(tokenizedMessageContent.get(j))){
                    validSyntax = true;
                    break;
                }
                else{
                    shortcode += tokenizedMessageContent.get(j);
                }
            }
            if (validSyntax && validEmojis.containsKey(shortcode)){
                tokenizedMessageContent.set(i, validEmojis.get(shortcode));
                for (int k = j; k > i; k--){
                    tokenizedMessageContent.remove(k);
                }
            }

        }
        if (linkPrefix.contains(tokenizedMessageContent.get(i))){
            tokenizedMessageContent.add(i, markToHtml.get("LINK")[0]);
            for (int j = i+1; j < tokenizedMessageContent.size(); j++){
                if (tokenizedMessageContent.get(j).equals(" ") || j == tokenizedMessageContent.size()-1){
                    if (tokenizedMessageContent.get(j).equals(" ")){
                        j--;
                    }
                    String linkContents = "";
                    for (int k = i+1; k < j+1; k++){
                        linkContents += tokenizedMessageContent.get(k);
                    }
                    tokenizedMessageContent.add(j+1, markToHtml.get("LINK")[1] + linkContents + markToHtml.get("LINK")[2]);
                    i++;
                    break;
                }
            }
        }
    }

    // converts ArrayList to string
    for (String token:tokenizedMessageContent){
        parsedMessageContent += token;
    }

    Message message =
        new Message(
            UUID.randomUUID(),
            conversation.getId(),
            user.getId(),
            parsedMessageContent,
            Instant.now());

    messageStore.addMessage(message);
    updateAuthorNumMessages(message);
    // redirect to a GET request
    response.sendRedirect("/chat/" + conversationTitle);
  }

  /** Increments the number of messages sent by the author of this message. */
  private void updateAuthorNumMessages(Message message){
    User authorUser = userStore.getUser(message.getAuthorId());
    authorUser.incrementNumMessages();
    userStore.updateUser(authorUser);
  }
}
