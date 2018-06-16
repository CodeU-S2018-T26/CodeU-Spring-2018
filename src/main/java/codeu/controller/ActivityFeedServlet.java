package codeu.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import codeu.model.data.Conversation;
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

  ArrayList<Instant> eventsInstantsSorted = new ArrayList<Instant>();

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
   * This function takes in a list of conversations,users and messages and returns a HashMap of
   * Instants mapping to HashMaps of UUIDs mapping to a data store string: User,Conversation or
   * Message
   * 
   * @param conversations
   * @param users
   * @param messages
   * @return built HashMap
   */
  HashMap<Instant, HashMap<UUID, String>> buildHashMap(List<Conversation> conversations,
      List<User> users, List<Message> messages) {
    HashMap<Instant, HashMap<UUID, String>> outerhm = new HashMap<Instant, HashMap<UUID, String>>();

    for (User user : users) {
      HashMap<UUID, String> innerhm = new HashMap<UUID, String>();
      innerhm.put(user.getId(), "user");
      outerhm.put(user.getCreationTime(), innerhm);

    }

    for (Conversation conversation : conversations) {
      HashMap<UUID, String> innerhm = new HashMap<UUID, String>();
      innerhm.put(conversation.getId(), "conversation");
      outerhm.put(conversation.getCreationTime(), innerhm);

    }
    for (Message message : messages) {
      HashMap<UUID, String> innerhm = new HashMap<UUID, String>();
      innerhm.put(message.getId(), "message");
      outerhm.put(message.getCreationTime(), innerhm);
    }
    return outerhm;
  }

  /**
   * This function takes in a HashMap and finds the earliest Instant using the isBefore function
   * 
   * @param hm
   * @return the earliest instant
   */
  Instant findEarliestInstant(HashMap<Instant, HashMap<UUID, String>> hm) {
    Instant earliestInstant = null;
    for (Map.Entry<Instant, HashMap<UUID, String>> m : hm.entrySet()) {
      earliestInstant = m.getKey();
      break;
    }
    for (Map.Entry<Instant, HashMap<UUID, String>> m : hm.entrySet()) {
      Instant toCmpInstant = m.getKey();
      if (earliestInstant.isBefore(toCmpInstant)) {
        earliestInstant = toCmpInstant;
      }
    }
    return earliestInstant;
  }

  /**
   * This function takes in a HashMap and sorts the instants from latest to oldest Stores Instants
   * in correct order in an ArrayList
   * 
   * @param hm
   * @return a sorted HashMap by Instants
   */
  HashMap<Instant, HashMap<UUID, String>> sortHashMap(HashMap<Instant, HashMap<UUID, String>> hm) {
    Instant earlier = null;
    int size = hm.size();
    eventsInstantsSorted = new ArrayList<Instant>();
    HashMap<Instant, HashMap<UUID, String>> sortedhm =
        new HashMap<Instant, HashMap<UUID, String>>();
    for (int i = 0; i < size; i++) {
      earlier = findEarliestInstant(hm);
      eventsInstantsSorted.add(earlier);
      HashMap<UUID, String> innersm = hm.get(earlier);
      sortedhm.put(earlier, innersm);
      hm.remove(earlier);
    }
    return sortedhm;
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
    List<User> users = userStore.getAllUsers();
    List<Conversation> conversations = conversationStore.getAllConversations();
    List<Message> messages = messageStore.getAllMessages();
    HashMap<Instant, HashMap<UUID, String>> eventsMap =
        buildHashMap(conversations, users, messages);
    eventsMap = sortHashMap(eventsMap);
    userStore.setEventsInstants(eventsInstantsSorted);
    request.setAttribute("users", users);
    request.setAttribute("conversations", conversations);
    request.setAttribute("messages", messages);
    //request.setAttribute("eventsInstantsSorted", eventsInstantsSorted);
    //request.setAttribute("eventsMap", eventsMap);
    request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
  }

}
