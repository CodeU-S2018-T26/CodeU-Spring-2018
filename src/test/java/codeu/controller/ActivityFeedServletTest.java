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
import codeu.model.data.Event;
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
  private ConversationStore mockConversationStore;
  private MessageStore mockMessageStore;
  private UserStore mockUserStore;
  private List<User> fakeUsers = new ArrayList<>();
  private List<Conversation> fakeConversations = new ArrayList<>();
  private List<Message> fakeMessages = new ArrayList<>();



  @Before
  public void setup() {
    activityfeedServlet = new ActivityFeedServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockConversationStore = Mockito.mock(ConversationStore.class);
    activityfeedServlet.setConversationStore(mockConversationStore);

    mockMessageStore = Mockito.mock(MessageStore.class);
    activityfeedServlet.setMessageStore(mockMessageStore);

    mockUserStore = Mockito.mock(UserStore.class);
    activityfeedServlet.setUserStore(mockUserStore);

  }

  @Test
  public void testDoGet() throws IOException, ServletException {

    // Initializing Fake DataStore ...
    UUID fakeUser1Id = UUID.randomUUID();
    Instant fakeUser1Instant = Instant.now();
    User fakeUser1 =
        new User(fakeUser1Id, "user1", "9dd32163318bf5624afd72990234ee99", fakeUser1Instant);
    UUID fakeUser2Id = UUID.randomUUID();
    Instant fakeUser2Instant = Instant.now().plusSeconds(2000);
    User fakeUser2 =
        new User(fakeUser2Id, "user2", "c941dacea833ab8f740103b7ab17b436", fakeUser2Instant);

    UUID fakeConversationId = UUID.randomUUID();
    Instant fakeConversationInstant = Instant.now().plusSeconds(1500);
    Conversation fakeConversation = new Conversation(fakeConversationId, fakeUser1Id,
        "conversationTitle", fakeConversationInstant);

    UUID fakeMessageId = UUID.randomUUID();
    Instant fakeMessageInstant = Instant.now().plusSeconds(4000);
    Message fakeMessage = new Message(fakeMessageId, fakeConversationId, fakeUser1Id,
        "messageContent", fakeMessageInstant);

    // Building Fake List of Data ...
    fakeUsers.add(fakeUser1);
    fakeUsers.add(fakeUser2);

    fakeConversations.add(fakeConversation);

    fakeMessages.add(fakeMessage);


    // Building Fake Array of Sorted Instants ...
    ArrayList<Instant> fakeEventsInstantsSorted = new ArrayList<Instant>();
    fakeEventsInstantsSorted.add(fakeUser1Instant);
    fakeEventsInstantsSorted.add(fakeConversationInstant);
    fakeEventsInstantsSorted.add(fakeUser2Instant);
    fakeEventsInstantsSorted.add(fakeMessageInstant);


    // Mocking ...
    Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUsers);
    Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversations);
    Mockito.when(mockMessageStore.getAllMessages()).thenReturn(fakeMessages);

    Mockito.when(mockUserStore.getAllEventsInstants()).thenReturn(fakeEventsInstantsSorted);

    activityfeedServlet.doGet(mockRequest, mockResponse);


    // Testing ...
    Mockito.verify(mockRequest).setAttribute("users", fakeUsers);
    Mockito.verify(mockRequest).setAttribute("conversations", fakeConversations);
    Mockito.verify(mockRequest).setAttribute("messages", fakeMessages);

    Mockito.verify(mockRequest).setAttribute("eventsInstantsSorted", fakeEventsInstantsSorted);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

}
