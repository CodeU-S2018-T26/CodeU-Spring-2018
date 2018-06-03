package codeu.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

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
public class ActivityFeedServlet extends HttpServlet 
{
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
   * This function takes in a list of conversations,users and messages and returns a HashMap of Instants mapping to 
   * HashMaps of UUIDs mapping to either User,Conversation or Message
   * @param conversations
   * @param users
   * @param messages
   * @return built HashMap
   */
  HashMap<Instant,HashMap<UUID,String>> buildHashMap(List<Conversation> conversations,List<User> users,List<Message> messages) {
	  HashMap<Instant,HashMap<UUID,String>> outerhm =new HashMap<Instant,HashMap<UUID,String>>();
	  for (User user : users) {
		  HashMap<UUID,String> innerhm=new HashMap<UUID,String>();
		  innerhm.put(user.getId(),"user"); 
		  outerhm.put(user.getCreationTime(), innerhm);
	  }
	  for (Conversation conversation : conversations) {
		  HashMap<UUID,String> innerhm=new HashMap<UUID,String>();
		  innerhm.put(conversation.getId(),"conversation"); 
		  outerhm.put(conversation.getCreationTime(), innerhm);
	  }
	  for (Message message : messages) {
		  HashMap<UUID,String> innerhm=new HashMap<UUID,String>();
		  innerhm.put(message.getId(),"message"); 
		  outerhm.put(message.getCreationTime(), innerhm);
	  }
	  return outerhm;
  }

  /**
   * This function takes in a HashMap and finds the earliest Instant using the isBefore function
   * @param hm
   * @return the earliest instant
   */
  Instant findEarliestInstant(HashMap<Instant,HashMap<UUID,String>> hm) {
	  	Instant earliestInstant=null;
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
   * This function takes in a HashMap and sorts the instants from latest to oldest
   * @param hm
   * @return a sorted HashMap by Instants
   */
  HashMap<Instant, HashMap<UUID, String>> sortHashMap(HashMap<Instant,HashMap<UUID,String>> hm)
  {
	  Instant earlier=null;
	  int size=hm.size();
	  HashMap<Instant,HashMap<UUID,String>> sortedhm=new HashMap<Instant,HashMap<UUID,String>>();
	  for (int i=0;i<size;i++) {	
		earlier=findEarliestInstant(hm);
		HashMap<UUID,String> innersm=hm.get(earlier);
		System.out.println("PRINTING OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		System.out.println("Instant:");
		System.out.println(earlier.toString());
	  	sortedhm.put(earlier, innersm);
		for (Map.Entry<UUID,String> im:innersm.entrySet()) {
			System.out.println("UUID:");
			System.out.println(im.getKey());
			System.out.println("Section:");
			System.out.println(im.getValue());
		}
		hm.remove(earlier);
	  }
	  return sortedhm;      
  }
  
	/**
	 * This function fires when a user requests the /activity URL. It simply
	 * forwards the request to activity.jsp.
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		List<User> users = userStore.getAllUsers();
		List<Conversation> conversations = conversationStore.getAllConversations();
	    List<Message> messages = MessageStore.getAllMessages();
	    HashMap<Instant,HashMap<UUID,String>> instantByInstance=buildHashMap(conversations,users,messages);
	    instantByInstance=sortHashMap(instantByInstance);
	    request.setAttribute("instantByInstance", instantByInstance);
	    request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);

	}
	
	
}