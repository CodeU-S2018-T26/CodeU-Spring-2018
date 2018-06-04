package codeu.controller;

import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AdminServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
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
   * Set up state for handling requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
  }

  static final List<String> ADMIN_USERNAMES = Arrays.asList("ayliana", "Marouane", "jeremy", "marissa", "raymond");

  //This function fires when a user requests the /admin page.
  //If the user is an admin it gets the number of users, conversations, and messages and forwards to admin.jsp
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them try to access the admin page
      response.sendRedirect("/login");
      return;
    }
    else if(! ADMIN_USERNAMES.contains(username)) {
      // user is not an admin, show error message
      request.setAttribute("error", "Only admins can access this page.");
    }

    else{
      int numUsers = userStore.getNumUsers();
      request.setAttribute("numUsers", numUsers);

      int numConversations = conversationStore.getNumConversations();
      request.setAttribute("numConversations", numConversations);

      int numMessages = messageStore.getNumMessages();
      request.setAttribute("numMessages", numMessages);

      String newestUser = userStore.newestUser().getName();
      request.setAttribute("newestUser", newestUser);
    }
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }


}
