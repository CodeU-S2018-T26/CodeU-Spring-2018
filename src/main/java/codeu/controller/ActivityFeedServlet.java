package codeu.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import codeu.model.data.Conversation;
import codeu.model.data.Event;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;

/** Servlet class responsible for the activity feed page. */
public class ActivityFeedServlet extends HttpServlet {
  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Set up state for handling chat requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
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
   * This function fires when a user requests the /activity URL. It simply forwards the request to
   * activity.jsp.
   * 
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    ArrayList<Instant> eventsInstantsSorted = new ArrayList<Instant>();
    HashMap<Instant, Event> eventsMap = new HashMap<Instant, Event>();

    List<User> users = userStore.getAllUsers();
    List<Conversation> conversations = conversationStore.getAllConversations();
    List<Message> messages = messageStore.getAllMessages();

    eventsMap = userStore.buildEventsMap(users, conversations, messages);
    eventsMap = userStore.sortEventsMap(eventsMap);
    eventsInstantsSorted = userStore.getAllEventsInstants();

    request.setAttribute("users", users);
    request.setAttribute("conversations", conversations);
    request.setAttribute("messages", messages);

    request.setAttribute("eventsInstantsSorted", eventsInstantsSorted);
    request.setAttribute("eventsMap", eventsMap);


    request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
  }

}
