package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminServletTest {
  private AdminServlet AdminServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private ConversationStore mockConversationStore;
  private UserStore mockUserStore;
  private MessageStore mockMessageStore;

  @Before
  public void setUp() {
    AdminServlet = new AdminServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/admin.jsp"))
      .thenReturn(mockRequestDispatcher);

    mockUserStore = Mockito.mock(UserStore.class);
    AdminServlet.setUserStore(mockUserStore);

    mockConversationStore = Mockito.mock(ConversationStore.class);
    AdminServlet.setConversationStore(mockConversationStore);

    mockMessageStore = Mockito.mock(MessageStore.class);
    AdminServlet.setMessageStore(mockMessageStore);
  }

  @Test
  public void testDoGet_UserNotLoggedIn() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

    AdminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockResponse).sendRedirect("/login");
  }

  @Test
  public void testDoGet_UserNotAdmin() throws IOException, ServletException{
    Mockito.when(mockSession.getAttribute("user")).thenReturn("User1");
    Mockito.when(mockSession.getAttribute("isAdmin")).thenReturn(false);

    AdminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("error", "Only admins can access this page.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoGet_UserAdmin() throws IOException, ServletException{
    Mockito.when(mockSession.getAttribute("user")).thenReturn("marissa");
    Mockito.when(mockSession.getAttribute("isAdmin")).thenReturn(true);
    UUID fakeUserId = UUID.randomUUID();
    User fakeUser =
      new User(
        fakeUserId,
        "marissa",
        "$2a$10$bBiLUAVmUFK6Iwg5rmpBUOIBW6rIMhU1eKfi3KR60V9UXaYTwPfHy",
        Instant.now());

    Mockito.when(mockUserStore.getUser("marissa")).thenReturn(fakeUser);
    Mockito.when(mockUserStore.getUser(fakeUserId)).thenReturn(fakeUser);
    Mockito.when(mockUserStore.newestUser()).thenReturn(fakeUser);
    Mockito.when(mockMessageStore.mostActiveUser()).thenReturn(fakeUserId);

    AdminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("numUsers", mockUserStore.getNumUsers());
    Mockito.verify(mockRequest).setAttribute("numConversations", mockConversationStore.getNumConversations());
    Mockito.verify(mockRequest).setAttribute("numMessages", mockMessageStore.getNumMessages());
    Mockito.verify(mockRequest).setAttribute("newestUser", "marissa");
    Mockito.verify(mockRequest).setAttribute("mostActiveUser", "marissa");

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
}
