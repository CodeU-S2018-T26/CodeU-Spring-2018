package codeu.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;

public class ActivityFeedServletTest {
  private ActivityFeedServlet activityfeedServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private HttpSession mockSession;
  private RequestDispatcher mockRequestDispatcher;
  private ActivityFeedServlet mockActivityfeedServlet;
  private ConversationStore mockConversationStore;
  private MessageStore mockMessageStore;
  private UserStore mockUserStore;
  private List<User> fakeUsers = new ArrayList<>();
  private List<Conversation> fakeConversations = new ArrayList<>();
  private List<Message> fakeMessages = new ArrayList<>();



  @Before
  public void setup() {
    activityfeedServlet = new ActivityFeedServlet();
    mockActivityfeedServlet = Mockito.mock(ActivityFeedServlet.class);

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockConversationStore = Mockito.mock(ConversationStore.class);
    mockActivityfeedServlet.setConversationStore(mockConversationStore);

    mockMessageStore = Mockito.mock(MessageStore.class);
    mockActivityfeedServlet.setMessageStore(mockMessageStore);

    mockUserStore = Mockito.mock(UserStore.class);
    mockActivityfeedServlet.setUserStore(mockUserStore);

  }

  @Ignore
  public void testDoGet() throws IOException, ServletException {

    // Initializing Fake DataStore ...
    UUID fakeUser1Id = UUID.randomUUID();
    Instant fakeUser1Instant = Instant.now();
    User fakeUser1 =
        new User(fakeUser1Id, "user1", "9dd32163318bf5624afd72990234ee99", fakeUser1Instant);
    mockUserStore.addUser(fakeUser1);
    UUID fakeUser2Id = UUID.randomUUID();
    Instant fakeUser2Instant = Instant.now().plusSeconds(2000);
    User fakeUser2 =
        new User(fakeUser2Id, "user2", "c941dacea833ab8f740103b7ab17b436", fakeUser2Instant);
    mockUserStore.addUser(fakeUser2);

    UUID fakeConversationId = UUID.randomUUID();
    Instant fakeConversationInstant = Instant.now().plusSeconds(1500);
    Conversation fakeConversation = new Conversation(fakeConversationId, fakeUser1Id,
        "conversationTitle", fakeConversationInstant);
    mockConversationStore.addConversation(fakeConversation);

    UUID fakeMessageId = UUID.randomUUID();
    Instant fakeMessageInstant = Instant.now().plusSeconds(4000);
    Message fakeMessage = new Message(fakeMessageId, fakeConversationId, fakeUser1Id,
        "messageContent", fakeMessageInstant);
    mockMessageStore.addMessage(fakeMessage);

    // Building Fake List of Data ...
    fakeUsers.add(fakeUser1);
    fakeUsers.add(fakeUser2);

    fakeConversations.add(fakeConversation);

    fakeMessages.add(fakeMessage);


    // Building Fake HashMap ...
    HashMap<Instant, HashMap<UUID, String>> fakeHashMap =
        new HashMap<Instant, HashMap<UUID, String>>();
    HashMap<UUID, String> fakeInnerHashMap = new HashMap<UUID, String>();
    fakeInnerHashMap.put(fakeUser1Id, "user");
    fakeHashMap.put(fakeUser1Instant, fakeInnerHashMap);
    fakeInnerHashMap = new HashMap<UUID, String>();
    fakeInnerHashMap.put(fakeUser2Id, "user");
    fakeHashMap.put(fakeUser2Instant, fakeInnerHashMap);
    fakeInnerHashMap = new HashMap<UUID, String>();
    fakeInnerHashMap.put(fakeConversationId, "conversation");
    fakeHashMap.put(fakeConversationInstant, fakeInnerHashMap);
    fakeInnerHashMap = new HashMap<UUID, String>();
    fakeInnerHashMap.put(fakeMessageId, "message");
    fakeHashMap.put(fakeMessageInstant, fakeInnerHashMap);

    // Building Fake Array of Sorted Instants ...
    ArrayList<Instant> fakeInstantsSorted = new ArrayList<Instant>();
    fakeInstantsSorted.add(fakeUser1Instant);
    fakeInstantsSorted.add(fakeConversationInstant);
    fakeInstantsSorted.add(fakeUser2Instant);
    fakeInstantsSorted.add(fakeMessageInstant);

    Mockito.when(mockActivityfeedServlet.buildHashMap(fakeConversations, fakeUsers, fakeMessages))
        .thenReturn(fakeHashMap);
    //Mockito.when(mockActivityfeedServlet.getSortedInstants()).thenReturn(fakeInstantsSorted);

    activityfeedServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("instantByInstance", fakeHashMap);
    Mockito.verify(mockRequest).setAttribute("arrInstantsSorted", fakeInstantsSorted);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

}
